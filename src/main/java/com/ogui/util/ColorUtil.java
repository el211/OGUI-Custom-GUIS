package com.ogui.util;

import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:(#[A-Fa-f0-9]{6}):(#[A-Fa-f0-9]{6})>([^<]+)</gradient>");
    private static final Pattern RAINBOW_PATTERN = Pattern.compile("<rainbow>([^<]+)</rainbow>");


    public static String color(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }

        message = processGradients(message);

        message = processRainbow(message);

        message = processHex(message);

        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }


    private static String processHex(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            try {
                ChatColor hexColor = ChatColor.of("#" + hexCode);
                matcher.appendReplacement(buffer, hexColor.toString());
            } catch (Exception e) {
                matcher.appendReplacement(buffer, matcher.group(0));
            }
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }


    private static String processGradients(String message) {
        Matcher matcher = GRADIENT_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String startHex = matcher.group(1);
            String endHex = matcher.group(2);
            String text = matcher.group(3);

            String gradientText = applyGradient(text, startHex, endHex);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(gradientText));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }


    private static String processRainbow(String message) {
        Matcher matcher = RAINBOW_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String text = matcher.group(1);
            String rainbowText = applyRainbow(text);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(rainbowText));
        }

        matcher.appendTail(buffer);
        return buffer.toString();
    }


    private static String applyGradient(String text, String startHex, String endHex) {
        Color startColor = hexToColor(startHex);
        Color endColor = hexToColor(endHex);

        if (startColor == null || endColor == null) {
            return text;
        }

        StringBuilder result = new StringBuilder();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            double ratio = (double) i / Math.max(1, length - 1);
            Color color = interpolateColor(startColor, endColor, ratio);

            ChatColor chatColor = ChatColor.of(color);
            result.append(chatColor).append(c);
        }

        return result.toString();
    }


    private static String applyRainbow(String text) {
        StringBuilder result = new StringBuilder();
        int length = text.length();

        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            float hue = (float) i / length;
            Color color = Color.getHSBColor(hue, 1.0f, 1.0f);

            ChatColor chatColor = ChatColor.of(color);
            result.append(chatColor).append(c);
        }

        return result.toString();
    }


    private static Color interpolateColor(Color start, Color end, double ratio) {
        int red = (int) (start.getRed() + ratio * (end.getRed() - start.getRed()));
        int green = (int) (start.getGreen() + ratio * (end.getGreen() - start.getGreen()));
        int blue = (int) (start.getBlue() + ratio * (end.getBlue() - start.getBlue()));

        return new Color(red, green, blue);
    }

    private static Color hexToColor(String hex) {
        try {
            // Remove # if present
            hex = hex.replace("#", "");

            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);

            return new Color(r, g, b);
        } catch (Exception e) {
            return null;
        }
    }


    public static String stripColor(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        text = text.replaceAll("&#[A-Fa-f0-9]{6}", "");

        text = text.replaceAll("<gradient:[^>]+>", "").replaceAll("</gradient>", "");

        text = text.replaceAll("<rainbow>", "").replaceAll("</rainbow>", "");

        return ChatColor.stripColor(text);
    }


    public static List<String> color(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return messages;
        }

        List<String> colored = new ArrayList<>();
        for (String message : messages) {
            colored.add(color(message));
        }
        return colored;
    }


    public static List<String> colorList(List<String> messages) {
        return color(messages);
    }

    public static boolean supportsRGB() {
        try {
            ChatColor.of("#FFFFFF");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}