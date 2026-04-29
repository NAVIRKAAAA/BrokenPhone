import {initializeApp} from "firebase-admin/app";

initializeApp();

export {handleSessionExpiry} from "./sessions/handleSessionExpiry";
export {sendNewsNotification} from "./notifications/sendNewsNotification";
