import {onRequest} from "firebase-functions/v2/https";
import {defineSecret} from "firebase-functions/params";
import {createClient} from "@supabase/supabase-js";

const supabaseUrl = defineSecret("SUPABASE_URL");
const supabaseKey = defineSecret("SUPABASE_SERVICE_ROLE_KEY");

export const handleSessionExpiry = onRequest(
  {
    timeoutSeconds: 540,
    memory: "256MiB",
    secrets: [supabaseUrl, supabaseKey],
  },
  async (req, res) => {
    const {sessionId, delayMs} = req.body;

    if (!sessionId || delayMs == null) {
      res.status(400).send("Missing sessionId or delayMs");
      return;
    }

    await new Promise((resolve) => setTimeout(resolve, delayMs));

    const supabase = createClient(
      supabaseUrl.value(),
      supabaseKey.value(),
    );

    await supabase.rpc("auto_cancel_session", {p_session_id: sessionId});

    res.send("ok");
  }
);
