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
    if (!(typeof data.text === "string") || data.text.length === 0) {
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
      from: `${APP_NAME} <tripfinity.developers@gmail.com>`,
      to: data.text, //sending to email IDs in app request, please check README.md
      subject: `Hello from ${APP_NAME}!`,
      text: `Hi,\n Test email from ${APP_NAME}.`
    };
  
    smtpTransport.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.log(error.message);
        smtpTransport.close();
      }
      return "mail sent";
    });
  });