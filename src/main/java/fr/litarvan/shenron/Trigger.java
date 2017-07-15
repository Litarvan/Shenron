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
package fr.litarvan.shenron;

public class Trigger
{
    private String phrase;
    private String image;
    private String message;

    public Trigger(String phrase, String image, String message)
    {
        this.phrase = phrase;
        this.image = image;
    }

    public String getPhrase()
    {
        return phrase;
    }

    public String getImage()
    {
        return image;
    }

    public String getMessage()
    {
        return message;
    }
}
