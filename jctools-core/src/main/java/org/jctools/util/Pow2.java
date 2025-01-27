/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jctools.util;

/**
 * Power of 2 utility functions.
 */
@InternalAPI
public final class Pow2 {
    public static final int MAX_POW2 = 1 << 30;

    /**
     * @param value from which next positive power of two will be found.
     * @return the next positive power of 2, this value if it is a power of 2. Negative values are mapped to 1.
     * @throws IllegalArgumentException is value is more than MAX_POW2 or less than 0
     */
    public static int roundToPowerOfTwo(final int value) {
        // 如果value > 2 ^ 30 或 value < 0 时将抛出IllegalArgumentException异常
        if (value > MAX_POW2) {
            throw new IllegalArgumentException("There is no larger power of 2 int for value:"+value+" since it exceeds 2^31.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Given value:"+value+". Expecting value >= 0.");
        }

        // int numberOfLeadingZeros(int i) 给定一个int类型数据，返回这个数据的二进制串中从最左边算起连续的“0”的总数量。因为int类型的数据长度为32所以高位不足的地方会以“0”填充。
        // 如 int x=1; int z=5;
        // System.out.println(x+"的二进制表示为："+Integer.toBinaryString(x)+"最左边开始数起连续的0的个数为："+Integer.numberOfLeadingZeros(x));
        // System.out.println(z+"的二进制表示为："+Integer.toBinaryString(z)+"最左边开始数起连续的0的个数为："+Integer.numberOfLeadingZeros(z));
        // 1的二进制表示为：1最左边开始数起连续的0的个数为：31
        // 5的二进制表示为：101最左边开始数起连续的0的个数为：29
        final int nextPow2 = 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
        return nextPow2;
    }

    /**
     * @param value to be tested to see if it is a power of two.
     * @return true if the value is a power of 2 otherwise false.
     */
    public static boolean isPowerOfTwo(final int value) {
        return (value & (value - 1)) == 0;
    }

    /**
     * Align a value to the next multiple up of alignment. If the value equals an alignment multiple then it
     * is returned unchanged.
     *
     * @param value to be aligned up.
     * @param alignment to be used, must be a power of 2.
     * @return the value aligned to the next boundary.
     */
    public static long align(final long value, final int alignment) {
        if (!isPowerOfTwo(alignment)) {
            throw new IllegalArgumentException("alignment must be a power of 2:" + alignment);
        }
        return (value + (alignment - 1)) & ~(alignment - 1);
    }
}
