"use strict";
const functions = require("firebase-functions");
const admin = require("firebase-admin");
const nodemailer = require("nodemailer");
const { google } = require("googleapis");

exports.sendemail = functions.https.onCall(async (data, context) => {
    const OAuth2 = google.auth.OAuth2;
    const APP_NAME = "Tripfinity";
    const clientID = process.env.CLIENT_ID;
    const clientSecret = process.env.CLIENT_SECRET;
    const refreshToken = process.env.REFRESH_TOKEN;
    
    // Checking attribute.`
    if (!(typeof data.receiver === "string") || data.receiver.length === 0) {
      // Throwing an HttpsError so that the client gets the error details.
      throw new functions.https.HttpsError(
        "invalid-argument",
        "The function must be called with one arguments containing the message text to add."
      );
    }
    // Checking that the user is authenticated.
    if (!context.auth) {
      // Throwing an HttpsError so that the client gets the error details.
      throw new functions.https.HttpsError(
        "failed-precondition",
        "The function must be called while authenticated."
      );
    }
    const oauth2Client = new OAuth2(
      clientID, //client Id
      clientSecret, // Client Secret
      "https://developers.google.com/oauthplayground" // Redirect URL
    );
  
    oauth2Client.setCredentials({
      refresh_token: refreshToken
    });
    const tokens = await oauth2Client.refreshAccessToken();
    const accessToken = tokens.credentials.access_token;
  
    const smtpTransport = nodemailer.createTransport({
      service: "gmail",
      auth: {
        type: "OAuth2",
        user: "tripfinity.developers@gmail.com",
        clientId: clientID,
        clientSecret: clientSecret,
        refreshToken: refreshToken,
        accessToken: accessToken
      }
    });
    const mailOptions = {
      from: `${data.sender} <tripfinity.developers@gmail.com>`,
      to: data.receiver, //sending to email IDs in app request, please check README.md
      subject: `Hello from ${APP_NAME}!`,
      html: `<html>
    
      Hi ${data.receiver},
    <br/>
    <br/>
    Your friend ${data.sender} has invited you to join ${APP_NAME}
    <br/>
    <br/>
    <img src="https://firebasestorage.googleapis.com/v0/b/tripfinity-3ccc3.appspot.com/o/tripfinity_icon.png?alt=media"/>
    </html>`
    };
  
    smtpTransport.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.log(error.message);
        smtpTransport.close();
      }
      return "mail sent";
    });
  });
