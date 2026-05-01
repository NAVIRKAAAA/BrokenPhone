import "@supabase/functions-js/edge-runtime.d.ts"
import { createClient } from "jsr:@supabase/supabase-js@2"

Deno.serve(async (req) => {
  const { session_id, delay_ms } = await req.json()

  await new Promise((resolve) => setTimeout(resolve, delay_ms))

  const supabase = createClient(
    Deno.env.get("SUPABASE_URL")!,
    Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
  )

  await supabase.rpc("auto_cancel_session", { p_session_id: session_id })

  return new Response("ok")
})