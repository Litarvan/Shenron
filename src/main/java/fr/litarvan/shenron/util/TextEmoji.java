/*
 * Copyright 2016-2017 Adrien 'Litarvan' Navratil
 *
 * This file is part of Shenron.
 *
 * Shenron is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Shenron is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Shenron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.litarvan.shenron.util;

import java.util.ArrayList;
import java.util.HashMap;

public enum TextEmoji
{
    A("a", "\uD83C\uDD70"),
    B("b", "\uD83C\uDD71"),
    M("m", "\u24C2"),
    O("o2", "\uD83C\uDD7E"),
    P("parking", "\uD83C\uDD7F"),
    ZERO("0", "\u0030\u20E3"),
    ONE("1", "\u0031\u20E3"),
    TWO("2", "\u0032\u20E3"),
    THREE("3", "\u0033\u20E3"),
    FOUR("4", "\u0034\u20E3"),
    FIVE("5", "\u0035\u20E3"),
    SIX("6", "\u0036\u20E3"),
    SEVEN("7", "\u0037\u20E3"),
    EIGHT("8", "\u0038\u20E3"),
    NINE("9", "\u0039\u20E3"),
    RI_A("regional_indicator_a", "\uD83C\uDDE6"),
    RI_B("regional_indicator_b", "\uD83C\uDDE7"),
    RI_C("regional_indicator_c", "\uD83C\uDDE8"),
    RI_D("regional_indicator_d", "\uD83C\uDDE9"),
    RI_E("regional_indicator_e", "\uD83C\uDDEA"),
    RI_F("regional_indicator_f", "\uD83C\uDDEB"),
    RI_G("regional_indicator_g", "\uD83C\uDDEC"),
    RI_H("regional_indicator_h", "\uD83C\uDDED"),
    RI_I("regional_indicator_i", "\uD83C\uDDEE"),
    RI_J("regional_indicator_j", "\uD83C\uDDEF"),
    RI_K("regional_indicator_k", "\uD83C\uDDF0"),
    RI_L("regional_indicator_l", "\uD83C\uDDF1"),
    RI_M("regional_indicator_m", "\uD83C\uDDF2"),
    RI_N("regional_indicator_n", "\uD83C\uDDF3"),
    RI_O("regional_indicator_o", "\uD83C\uDDF4"),
    RI_P("regional_indicator_p", "\uD83C\uDDF5"),
    RI_Q("regional_indicator_q", "\uD83C\uDDF6"),
    RI_R("regional_indicator_r", "\uD83C\uDDF7"),
    RI_S("regional_indicator_s", "\uD83C\uDDF8"),
    RI_T("regional_indicator_t", "\uD83C\uDDF9"),
    RI_U("regional_indicator_u", "\uD83C\uDDFA"),
    RI_V("regional_indicator_v", "\uD83C\uDDFB"),
    RI_W("regional_indicator_w", "\uD83C\uDDFC"),
    RI_X("regional_indicator_x", "\uD83C\uDDFD"),
    RI_Y("regional_indicator_y", "\uD83C\uDDFE"),
    RI_Z("regional_indicator_z", "\uD83C\uDDFF");

    public static final HashMap<Character, TextEmoji[]> CHAR_TO_EMOJI = new HashMap<>(); static {
        CHAR_TO_EMOJI.put('a', new TextEmoji[] {RI_A, A, FOUR});
        CHAR_TO_EMOJI.put('b', new TextEmoji[] {RI_B, B});
        CHAR_TO_EMOJI.put('c', new TextEmoji[] {RI_C});
        CHAR_TO_EMOJI.put('d', new TextEmoji[] {RI_D});
        CHAR_TO_EMOJI.put('e', new TextEmoji[] {RI_E, THREE});
        CHAR_TO_EMOJI.put('f', new TextEmoji[] {RI_F});
        CHAR_TO_EMOJI.put('g', new TextEmoji[] {RI_G});
        CHAR_TO_EMOJI.put('h', new TextEmoji[] {RI_H});
        CHAR_TO_EMOJI.put('i', new TextEmoji[] {RI_I, ONE});
        CHAR_TO_EMOJI.put('j', new TextEmoji[] {RI_J});
        CHAR_TO_EMOJI.put('k', new TextEmoji[] {RI_K});
        CHAR_TO_EMOJI.put('l', new TextEmoji[] {RI_L, SEVEN});
        CHAR_TO_EMOJI.put('m', new TextEmoji[] {RI_M, M});
        CHAR_TO_EMOJI.put('n', new TextEmoji[] {RI_N});
        CHAR_TO_EMOJI.put('o', new TextEmoji[] {RI_O, O, ZERO});
        CHAR_TO_EMOJI.put('p', new TextEmoji[] {RI_P, P});
        CHAR_TO_EMOJI.put('q', new TextEmoji[] {RI_Q});
        CHAR_TO_EMOJI.put('r', new TextEmoji[] {RI_R});
        CHAR_TO_EMOJI.put('s', new TextEmoji[] {RI_S});
        CHAR_TO_EMOJI.put('t', new TextEmoji[] {RI_T});
        CHAR_TO_EMOJI.put('u', new TextEmoji[] {RI_U});
        CHAR_TO_EMOJI.put('v', new TextEmoji[] {RI_V});
        CHAR_TO_EMOJI.put('w', new TextEmoji[] {RI_W});
        CHAR_TO_EMOJI.put('x', new TextEmoji[] {RI_X});
        CHAR_TO_EMOJI.put('y', new TextEmoji[] {RI_Y});
        CHAR_TO_EMOJI.put('z', new TextEmoji[] {RI_Z});
        CHAR_TO_EMOJI.put('1', new TextEmoji[] {ONE});
        CHAR_TO_EMOJI.put('2', new TextEmoji[] {TWO});
        CHAR_TO_EMOJI.put('3', new TextEmoji[] {THREE});
        CHAR_TO_EMOJI.put('4', new TextEmoji[] {FOUR});
        CHAR_TO_EMOJI.put('5', new TextEmoji[] {FIVE});
        CHAR_TO_EMOJI.put('6', new TextEmoji[] {SIX});
        CHAR_TO_EMOJI.put('7', new TextEmoji[] {SEVEN});
        CHAR_TO_EMOJI.put('8', new TextEmoji[] {EIGHT});
        CHAR_TO_EMOJI.put('9', new TextEmoji[] {NINE});
    }

    private String tag;
    private String unicode;

    TextEmoji(String tag, String unicode)
    {
        this.tag = ":" + tag + ":";
        this.unicode = unicode;
    }

    public String getTag()
    {
        return tag;
    }

    public String getUnicode()
    {
        return unicode;
    }

    public static TextEmoji[] toEmoji(String str)
    {
        return toEmoji(str, false);
    }

    public static TextEmoji[] toEmoji(String str, boolean useOnlyOneTime)
    {
        ArrayList<TextEmoji> result = new ArrayList<>();
        ArrayList<TextEmoji> used = new ArrayList<>();

        for (char c : str.toCharArray())
        {
            if (!CHAR_TO_EMOJI.containsKey(c))
            {
                continue;
            }

            TextEmoji[] emojis = CHAR_TO_EMOJI.get(c);
            TextEmoji emoji = null;

            if (useOnlyOneTime)
            {
                for (int i = 0; i < emojis.length; i++)
                {
                    if (!used.contains(emojis[i]))
                    {
                        emoji = emojis[i];
                        break;
                    }
                }
            }
            else
            {
                emoji = emojis[0];
            }

            if (emoji != null)
            {
                result.add(emoji);
                used.add(emoji);
            }
        }

        return result.toArray(new TextEmoji[result.size()]);
    }
}
