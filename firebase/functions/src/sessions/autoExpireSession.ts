import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {getFirestore, FieldValue, Timestamp} from "firebase-admin/firestore";

const db = getFirestore();

const POSTS_COLLECTION = "posts";
const USERS_COLLECTION = "users";

export const autoExpireSession = onDocumentCreated(
  {
    document: "sessions/{sessionId}",
    timeoutSeconds: 540,
    memory: "256MiB",
  },
  async (event) => {
    const sessionId = event.params.sessionId;
    const data = event.data?.data();
    if (!data) return;

    const {postId, userId, expiresAt, status} = data;

    if (status !== "ACTIVE") return;

    const expiresAtMs = expiresAt instanceof Timestamp ?
      expiresAt.toMillis() :
      (expiresAt as number);

    const delayMs = Math.max(expiresAtMs - Date.now(), 0);
    await new Promise((resolve) => setTimeout(resolve, delayMs));

    // Re-read session — may already be completed or cancelled by user
    const sessionSnap = await db.collection("sessions").doc(sessionId).get();
    const freshSession = sessionSnap.data();
    if (!freshSession || freshSession.status !== "ACTIVE") return;

    const historyItem = {
      sessionId,
      userId,
      type: "AUTO_CANCEL",
      timestamp: Timestamp.now(),
    };

    const sessionRef = db.collection("sessions").doc(sessionId);
    const postRef = db.collection(POSTS_COLLECTION).doc(postId as string);
    const userRef = db.collection(USERS_COLLECTION).doc(userId as string);

    await db.runTransaction(async (tx) => {
      const sessionDoc = await tx.get(sessionRef);
      const session = sessionDoc.data();

      // Final check inside transaction — guard against race conditions
      if (!session || session.status !== "ACTIVE") return;

      tx.update(sessionRef, {status: "AUTO_CANCELLED"});
      tx.update(postRef, {
        status: "AVAILABLE",
        sessionId: null,
        sessionsHistory: FieldValue.arrayUnion(historyItem),
      });
      tx.update(userRef, {sessionId: null});
    });
  }
);
