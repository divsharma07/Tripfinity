const functions = require("firebase-functions");
const admin = require("firebase-admin");
const fcm = admin.messaging();

exports.invitenotification = functions.https.onCall(async (data, context) => {
    if (!(typeof data.sender === "string") || data.sender.length === 0 || !(typeof data.trip === "string") || data.trip.length === 0 
        || !(typeof data.token === "string") || data.token.length === 0) {
        throw new functions.https.HttpsError(
          "invalid-argument",
          "The function must be called with one arguments containing the message text to add."
        );
    }

    if (!context.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new functions.https.HttpsError(
          "failed-precondition",
          "The function must be called while authenticated."
        );
      }

    const payload = {
        notification: {
          title: `Your friend ${data.sender} invited you to ${data.trip}`,
          body: `Start planning now!`,
        },
      };
    
    const options = {
        collapseKey: data.sender,
        priority: 'high',
    };
     
    fcm.sendToDevice(data.token, payload, options);
});