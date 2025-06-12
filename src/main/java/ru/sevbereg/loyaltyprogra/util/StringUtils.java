package ru.sevbereg.loyaltyprogra.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String escapeMarkdownV2(String text) {
        // Символы, которые нужно экранировать
        String unsafe = "_[]()~`>#+-=|{}.!\\";
        StringBuilder result = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (unsafe.indexOf(c) != -1) {
                result.append('\\');
            }
            result.append(c);
        }

        return result.toString();
    }

    public static String escapeMarkdownPreservingFormatting(String text) {
// Регулярка на MarkdownV2 formatting: *bold*, _italic_, ~strike~
        String formattingRegex = "(\\*[^*]+\\*|_[^_]+_|~[^~]+~)";
        Pattern pattern = Pattern.compile(formattingRegex);
        Matcher matcher = pattern.matcher(text);

        StringBuilder escaped = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            // Экранируем всё ДО форматированного блока
            String before = text.substring(lastEnd, matcher.start());
            escaped.append(escapeMarkdownV2(before));

            // Добавляем сам форматированный блок без изменений
            escaped.append(matcher.group());

            lastEnd = matcher.end();
        }

        // Экранируем остаток
        String after = text.substring(lastEnd);
        escaped.append(escapeMarkdownV2(after));

        return escaped.toString();
    }

}
