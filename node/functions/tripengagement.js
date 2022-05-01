const functions = require("firebase-functions");
const admin = require("firebase-admin");
const fcm = admin.messaging();

exports.tripengagement = functions.firestore
    .document("Trips/{tripIdentifier}")
    .onCreate(async (snapshot, context) => {
      const trip = snapshot.data();
      const destination = trip.destination;
      const sourceLocation = trip.sourceLocation;
      console.log("this is the sourceLocation" + sourceLocation);
      const payload = {
        notification: {
          title: `Let's Go!!!`,
          body: `Users around you are traveling to ${destination}. Pack your bags and prepare for an unforgettable expedition.`,
        },
      };
      fcm.sendToTopic(sourceLocation, payload);
    });
