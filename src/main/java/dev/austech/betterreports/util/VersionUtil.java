/*
 * BetterReports - VersionUtil.java
 *
 * Copyright (c) 2023 AusTech Development
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.austech.betterreports.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class VersionUtil {
    private V serverVersion = null;

    public String getPackageVersion() {
        final String packageName = Bukkit.getServer().getClass().getPackage().getName(); // CraftServer
        return packageName.substring(23);
    }

    public V getVersion() {
        if (serverVersion == null) {
            final String packageName = Bukkit.getServer().getClass().getPackage().getName(); // CraftServer
            serverVersion = V.parse(Integer.parseInt(packageName.substring(packageName.lastIndexOf('.') + 1).split("_")[1]));
        }
        return serverVersion;
    }

    @RequiredArgsConstructor
    public enum V {
        V1_7(7),
        V1_8(8),
        V1_9(9),
        V1_10(10),
        V1_11(11),
        V1_12(12),
        V1_13(13),
        V1_14(14),
        V1_15(15),
        V1_16(16),
        V1_17(17),
        V1_18(18),
        V1_19(19),
        V1_20(20),
        V1_21(21),

        UNKNOWN(0);

        private final int packageVersion;

        private static V parse(final int number) {
            for (final V v : values())
                if (v.packageVersion == number)
                    return v;

            return UNKNOWN;
        }

        public boolean olderThan(final V v) {
            return this.packageVersion < v.packageVersion;
        }

        public boolean newerThan(final V v) {
            return this.packageVersion > v.packageVersion;
        }

        @Override
        public String toString() {
            return "1." + this.packageVersion;
        }
    }
}
