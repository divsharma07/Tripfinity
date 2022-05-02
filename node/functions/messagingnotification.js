const functions = require("firebase-functions");
const admin = require("firebase-admin");
const fcm = admin.messaging();

exports.messagingnotification = functions.firestore
    .document("Trips/{tripId}/messages/{messageId}")
    .onCreate(async (snapshot, context) => {
      const tripRef = snapshot.ref.parent.parent;
      const message = snapshot.data();
      const senderName = message.senderName;
      
      tripRef.get().then(tripSnapshot => {
          const trip = tripSnapshot.data();
          const userRefArr = trip.users;
          const tripName = trip.tripName;
          const payload = {
            notification: {
              title: `Message received in ${tripName} by ${senderName}`,
              body: `${message.content}`,
              image: message.senderPhotoUrl,
            },
          };
          userRefArr.forEach(userRef => {
            userRef.get().then(userSnapshot => {
                if (userSnapshot.exists) {
                    const user = userSnapshot.data();
                    const token = user.fcmToken
                    if (user.name != message.senderName && token !== 'undefined') {
                        fcm.sendToDevice(token, payload);
                    }
                } else {
                    console.log("No such user!");
                }
            }).catch(error => {
                console.log("Error getting user:", error);
            });
          })
      })
    });