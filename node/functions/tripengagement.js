const functions = require("firebase-functions");
const admin = require("firebase-admin");
const { doc, query, collection, where, getDoc } = require("@firebase/firestore");
const fcm = admin.messaging();

exports.tripengagement = functions.firestore
    .document("Trips/{tripIdentifier}")
    .onCreate(async (snapshot, context) => {
      const tripId = context.params.tripIdValue;
      const trip = snapshot.val()
      const itinerarySnapshot = query(
        collection(db, "Itinerary"),
        where("trip", "==", tripIdentifier)
      );

      const itinerary = await getDoc(q);
      const place = itinerary.places[0].city

      // get the destination from the itinerary
      const topic = trip.sourceLocation

      // get the place from the user that created the trip which would be the topic this goes to

      const payload = {
        notification: {
          title: `Pack your bags and prepare for an adventure`,
          body: `Users around you are traveling to ${place}`,
          clickAction: "RECEIVE_ACTIVITY",
        },
      };
      fcm.sendToTopic(topic, payload);
    });
