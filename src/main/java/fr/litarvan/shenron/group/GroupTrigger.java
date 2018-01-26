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
package fr.litarvan.shenron.group;

import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class GroupTrigger
{
    private String messageId;
    private List<ImmutablePair<String, String>> groups;

    public GroupTrigger(String messageId, List<ImmutablePair<String, String>> groups)
    {
        this.messageId = messageId;
        this.groups = groups;

        // /group trigger "__Systèmes__ : Windows, Mac OS X, Linux" Windows/windows MacOS/osx Linux/linux
        // /group trigger "__Langages Web__ : HTML/CSS, Javascript, PHP" HTML#CSS/html Javascript/js PHP/php
        // /group trigger "__Langages Natifs__ : C, C++, Rust, Go, Bas-Level" C-lang/c_lang C++/cpp Rust/rust Go/go Bas-Level/baslvl
        // /group trigger "__Langages Haut-Niveau__ : Java, Lua, Ruby, Python" Java/java Lua/lua Ruby/ruby Python/python
        // /group trigger "__Langages fonctionnels__ : Haskell, Elixir" Haskell/haskell Elixir/elixir
        // /group trigger "__Autres groupes__ : Cryptomonnaie, Pervers" Cryptomonnaie/cmonnaie Pervers/kreygasm
    }

    public String getMessageId()
    {
        return messageId;
    }

    public List<ImmutablePair<String, String>> getGroups()
    {
        return groups;
    }
}
