package com.sorushi.invoice.management.usermanagement.utils;

public class MailerUtil {

  private MailerUtil() {}

  public static String buildOtpHtmlTemplate(String otp) {
    return """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); padding: 20px;">
                <h2 style="color: #333333;">Sorushi Invoice System</h2>
                <p style="font-size: 16px; color: #555555;">
                    Hello,
                </p>
                <p style="font-size: 16px; color: #555555;">
                    Your One-Time Password (OTP) for account activation is:
                </p>
                <div style="text-align: center; margin: 20px 0;">
                    <span style="font-size: 28px; color: #000000; font-weight: bold;">"""
        + otp
        + """
                </span>
                </div>
                <p style="font-size: 14px; color: #777777;">
                    This OTP is valid for <strong>15 minutes</strong>. Please do not share it with anyone.
                </p>
                <p style="font-size: 14px; color: #777777;">
                    If you did not request this OTP, please ignore this email.
                </p>
                <hr style="margin: 30px 0;">
                <p style="font-size: 12px; color: #aaaaaa; text-align: center;">
                    © 2025 Sorushi Technologies. All rights reserved.
                </p>
            </div>
        </body>
        </html>
        """;
  }
}
