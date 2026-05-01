import {onRequest} from "firebase-functions/v2/https";
import {defineSecret} from "firebase-functions/params";
import {createClient} from "@supabase/supabase-js";
import {getMessaging} from "firebase-admin/messaging";

const supabaseUrl = defineSecret("SUPABASE_URL");
const supabaseKey = defineSecret("SUPABASE_SERVICE_ROLE_KEY");
const adminSecret = defineSecret("ADMIN_SECRET");

const FCM_BATCH_SIZE = 500;

export const sendNewsNotification = onRequest(
  {secrets: [supabaseUrl, supabaseKey, adminSecret]},
  async (req, res) => {
    if (req.method !== "POST") {
      res.status(405).send("Method Not Allowed");
      return;
    }

    if (req.headers["x-admin-secret"] !== adminSecret.value()) {
      res.status(403).send("Forbidden");
      return;
    }

    const {title, body} = req.body as {title?: string; body?: string};
    if (!title?.trim() || !body?.trim()) {
      res.status(400).send("title and body are required");
      return;
    }

    const supabase = createClient(supabaseUrl.value(), supabaseKey.value());

    const {data: users, error: usersError} = await supabase
      .from("users")
      .select("id, fcm_token, permissions")
      .not("fcm_token", "is", null)
      .contains("notifications", ["NEWS"]);

    if (usersError) {
      res.status(500).send(usersError.message);
      return;
    }

    if (!users || users.length === 0) {
      res.json({sent: 0});
      return;
    }

    const pushUsers = users.filter(
      (u) => u.permissions?.isNotificationsGranted === true
    );
    const receiverIds = users.map((u) => u.id as string);
    const notificationId = crypto.randomUUID();

    const tokens = pushUsers.map((u) => u.fcm_token as string);
    for (let i = 0; i < tokens.length; i += FCM_BATCH_SIZE) {
      await getMessaging().sendEachForMulticast({
        tokens: tokens.slice(i, i + FCM_BATCH_SIZE),
        notification: {title, body},
        data: {type: "NEWS", notificationId},
      });
    }

    const {error: insertError} = await supabase.from("notifications").insert({
      id: notificationId,
      receiver_ids: receiverIds,
      type: "NEWS",
      title,
      body,
      created_at: Date.now(),
    });

    if (insertError) {
      res.status(500).send(insertError.message);
      return;
    }

    res.json({sent: tokens.length, total: receiverIds.length});
  }
);
