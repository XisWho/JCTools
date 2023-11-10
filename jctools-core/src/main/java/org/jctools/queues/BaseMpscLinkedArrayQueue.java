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
package org.jctools.queues;

import org.jctools.queues.IndexedQueueSizeUtil.IndexedQueue;
import org.jctools.util.PortableJvmInfo;
import org.jctools.util.Pow2;
import org.jctools.util.RangeUtil;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.jctools.queues.LinkedArrayQueueUtil.length;
import static org.jctools.queues.LinkedArrayQueueUtil.modifiedCalcCircularRefElementOffset;
import static org.jctools.util.UnsafeAccess.UNSAFE;
import static org.jctools.util.UnsafeAccess.fieldOffset;
import static org.jctools.util.UnsafeRefArrayAccess.*;


abstract class BaseMpscLinkedArrayQueuePad1<E> extends AbstractQueue<E> implements IndexedQueue
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    byte b170,b171,b172,b173,b174,b175,b176,b177;//128b
}

// $gen:ordered-fields
abstract class BaseMpscLinkedArrayQueueProducerFields<E> extends BaseMpscLinkedArrayQueuePad1<E>
{
    private final static long P_INDEX_OFFSET = fieldOffset(BaseMpscLinkedArrayQueueProducerFields.class, "producerIndex");

    private volatile long producerIndex;

    @Override
    public final long lvProducerIndex()
    {
        return producerIndex;
    }

    final void soProducerIndex(long newValue)
    {
        UNSAFE.putOrderedLong(this, P_INDEX_OFFSET, newValue);
    }

    final boolean casProducerIndex(long expect, long newValue)
    {
        return UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
    }
}

abstract class BaseMpscLinkedArrayQueuePad2<E> extends BaseMpscLinkedArrayQueueProducerFields<E>
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    byte b170,b171,b172,b173,b174,b175,b176,b177;//128b
}

// $gen:ordered-fields
abstract class BaseMpscLinkedArrayQueueConsumerFields<E> extends BaseMpscLinkedArrayQueuePad2<E>
{
    private final static long C_INDEX_OFFSET = fieldOffset(BaseMpscLinkedArrayQueueConsumerFields.class,"consumerIndex");

    private volatile long consumerIndex;
    protected long consumerMask;
    protected E[] consumerBuffer;

    @Override
    public final long lvConsumerIndex()
    {
        return consumerIndex;
    }

    final long lpConsumerIndex()
    {
        return UNSAFE.getLong(this, C_INDEX_OFFSET);
    }

    final void soConsumerIndex(long newValue)
    {
        UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, newValue);
    }
}

abstract class BaseMpscLinkedArrayQueuePad3<E> extends BaseMpscLinkedArrayQueueConsumerFields<E>
{
    byte b000,b001,b002,b003,b004,b005,b006,b007;//  8b
    byte b010,b011,b012,b013,b014,b015,b016,b017;// 16b
    byte b020,b021,b022,b023,b024,b025,b026,b027;// 24b
    byte b030,b031,b032,b033,b034,b035,b036,b037;// 32b
    byte b040,b041,b042,b043,b044,b045,b046,b047;// 40b
    byte b050,b051,b052,b053,b054,b055,b056,b057;// 48b
    byte b060,b061,b062,b063,b064,b065,b066,b067;// 56b
    byte b070,b071,b072,b073,b074,b075,b076,b077;// 64b
    byte b100,b101,b102,b103,b104,b105,b106,b107;// 72b
    byte b110,b111,b112,b113,b114,b115,b116,b117;// 80b
    byte b120,b121,b122,b123,b124,b125,b126,b127;// 88b
    byte b130,b131,b132,b133,b134,b135,b136,b137;// 96b
    byte b140,b141,b142,b143,b144,b145,b146,b147;//104b
    byte b150,b151,b152,b153,b154,b155,b156,b157;//112b
    byte b160,b161,b162,b163,b164,b165,b166,b167;//120b
    byte b170,b171,b172,b173,b174,b175,b176,b177;//128b
}

// $gen:ordered-fields
abstract class BaseMpscLinkedArrayQueueColdProducerFields<E> extends BaseMpscLinkedArrayQueuePad3<E>
{
    private final static long P_LIMIT_OFFSET = fieldOffset(BaseMpscLinkedArrayQueueColdProducerFields.class,"producerLimit");

    private volatile long producerLimit;
    protected long producerMask;
    protected E[] producerBuffer;

    final long lvProducerLimit()
    {
        return producerLimit;
    }

    final boolean casProducerLimit(long expect, long newValue)
    {
        return UNSAFE.compareAndSwapLong(this, P_LIMIT_OFFSET, expect, newValue);
    }

    final void soProducerLimit(long newValue)
    {
        UNSAFE.putOrderedLong(this, P_LIMIT_OFFSET, newValue);
    }
}


/**
 * An MPSC array queue which starts at <i>initialCapacity</i> and grows to <i>maxCapacity</i> in linked chunks
 * of the initial size. The queue grows only when the current buffer is full and elements are not copied on
 * resize, instead a link to the new buffer is stored in the old buffer for the consumer to follow.
 */
abstract class BaseMpscLinkedArrayQueue<E> extends BaseMpscLinkedArrayQueueColdProducerFields<E>
    implements MessagePassingQueue<E>, QueueProgressIndicators
{
    // No post padding here, subclasses must add
    private static final Object JUMP = new Object();
    private static final Object BUFFER_CONSUMED = new Object();
    private static final int CONTINUE_TO_P_INDEX_CAS = 0;
    private static final int RETRY = 1;
    private static final int QUEUE_FULL = 2;
    private static final int QUEUE_RESIZE = 3;


    /**
     * @param initialCapacity the queue initial capacity. If chunk size is fixed this will be the chunk size.
     *                        Must be 2 or more.
     */
    public BaseMpscLinkedArrayQueue(final int initialCapacity)
    {
        // initialCapacity必须大于等于2
        RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");

        // 容量确保是2的幂次方数，找到initialCapacity下一个2的幂次方数
        int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
        // leave lower bit of mask clear
        // index以2为步长增长，预留一个元素存JUMP，所以limit为（capacity-1）*2
        long mask = (p2capacity - 1) << 1;
        // need extra element to point at next array
        // 需要一个额外元素来链接下一个数组
        // 实际数组大小还是initialCapacity
        E[] buffer = allocateRefArray(p2capacity + 1);
        // 目前生产者和消费者Buffer指向同一个数组
        producerBuffer = buffer;
        producerMask = mask;
        consumerBuffer = buffer;
        consumerMask = mask;
        // 设置producerLimit=mask
        soProducerLimit(mask); // we know it's all empty to start with
    }

    public static void main(String[] args) {
        int p2capacity = Pow2.roundToPowerOfTwo(100);
        // leave lower bit of mask clear
        long mask = (p2capacity - 1) << 1;
        System.out.println(p2capacity);  // 128     10000000
        System.out.println(mask);   // 254          11111110
    }

    @Override
    public int size()
    {
        return IndexedQueueSizeUtil.size(this, IndexedQueueSizeUtil.IGNORE_PARITY_DIVISOR);
    }

    @Override
    public boolean isEmpty()
    {
        // Order matters!
        // Loading consumer before producer allows for producer increments after consumer index is read.
        // This ensures this method is conservative in it's estimate. Note that as this is an MPMC there is
        // nothing we can do to make this an exact method.
        return ((lvConsumerIndex() - lvProducerIndex()) / 2 == 0);
    }

    @Override
    public String toString()
    {
        return this.getClass().getName();
    }

    @Override
    public boolean offer(final E e)
    {
        if (null == e)
        {
            throw new NullPointerException();
        }

        long mask;
        E[] buffer;
        long pIndex;

        while (true)
        {
            // 获取生产者索引的最大限制，即producerLimit
            long producerLimit = lvProducerLimit();
            // 获取生产者索引，即producerIndex
            pIndex = lvProducerIndex();
            // lower bit is indicative of resize, if we see it we spin until it's cleared
            // 生产索引以2为步长递增，
            // 第0位标识为resize，所以非扩容场景，不会是奇数，
            // 扩容的时候，会在offerSlowPath()中扩容线程会将其设为奇数
            if ((pIndex & 1) == 1)
            {
                // 奇数代表正在扩容，自旋，等待扩容完成
                continue;
            }
            // pIndex is even (lower bit is 0) -> actual index is (pIndex >> 1)
            // pIndex 是偶数， 实际的索引值 需要 除以2

            // mask/buffer may get changed by resizing -> only use for array access after successful CAS.
            mask = this.producerMask;
            buffer = this.producerBuffer;
            // a successful CAS ties the ordering, lv(pIndex) - [mask/buffer] -> cas(pIndex)

            // assumption behind this optimization is that queue is almost always empty or near empty
            // 阈值 producerLimit 小于等于生产者指针位置 pIndex时，需要扩容
            // 此时还没有将e插入到队列中
            if (producerLimit <= pIndex)
            {
                // 通过offerSlowPath返回状态值，来查看怎么来处理这个待添加的元素
                int result = offerSlowPath(mask, pIndex, producerLimit);
                switch (result)
                {
                    case CONTINUE_TO_P_INDEX_CAS:
                        break;
                    case RETRY:
                        continue;
                    case QUEUE_FULL:
                        return false;
                    case QUEUE_RESIZE:
                        resize(mask, buffer, pIndex, e, null);
                        return true;
                }
            }

            // 阈值 producerLimit 大于 生产者指针位置 pIndex
            // 直接通过CAS操作对pIndex做加2处理
            if (casProducerIndex(pIndex, pIndex + 2))
            {
                break;
            }
        }

        // INDEX visible before ELEMENT
        // 将buffer数组的指定位置替换为e，
        // 不是根据下标来设置的，是根据槽位的地址偏移量offset，UNSAFE操作。
        final long offset = modifiedCalcCircularRefElementOffset(pIndex, mask);
        soRefElement(buffer, offset, e); // release element e
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single consumer thread use only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E poll()
    {
        // 存储元素的数组
        final E[] buffer = consumerBuffer;
        // 读取consumerIndex的值，注意这里是lp不是lv
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        // 计算在数组中的偏移量
        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        // 取元素，前面通过StoreStore写入的，这里通过LoadLoad取出来的就是最新值
        Object e = lvRefElement(buffer, offset);
        if (e == null)
        {
            long pIndex = lvProducerIndex();
            // isEmpty?
            // 队列中一个元素都没有了，则直接返回
            if ((cIndex - pIndex) / 2 == 0)
            {
                return null;
            }
            // poll() == null iff queue is empty, null element is not strong enough indicator, so we must
            // spin until element is visible.
            // 我觉得代码执行到这里有两种情况
            // 第一种，假如cIndex = 0，从来没有消费过
            //      第一步生产者代码 casProducerIndex(pIndex, pIndex + 2) ，将producerIndex = producerIndex + 2 = 2 。
            //      第二步，消费者代码执行
            //          offset = modifiedCalcCircularRefElementOffset(cIndex, mask)
            //          e = lvRefElement(buffer, offset) ，并且 e == null
            //          long pIndex = lvProducerIndex();
            //          (cIndex - pIndex) / 2 等价于 0 - 2 / 2 = -1 ，执行下面代码块，进入死循环
            //          直到生产者代码soRefElement(buffer, offset, e) 执行完毕
            //      此时e !=null ，退出下面死循环，这样做的目的，是因为生产者代码casProducerIndex(pIndex, pIndex + 2) 和
            //      soRefElement(buffer, offset, e) 不是原子性，因此消费者代码需要用这种补偿机制，但从代码设计角度上来看
            // Netty 这样做也是为了提升代码的性能。

            // 第二种情况 ：
            //      两个消费者线程同时执行到final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
            //      这一行代码线程1 执行了
            //          Object e = lvRefElement(buffer, offset);
            //          soRefElement(buffer, offset, null); 下边的代码，此时 offset位置的元素被置空
            //      此时线程2才执行
            //          Object e = lvRefElement(buffer, offset); ,e == null
            //      队列中还有其他元素  (cIndex - pIndex) / 2 !=0
            //      则线程2会进入下面代码块，一直死循环等待，直接offset 位置被设置了元素
            do
            {
                e = lvRefElement(buffer, offset);
            }
            while (e == null);
        }

        // 如果当前元素是JUMP，是到next数组中查找元素
        if (e == JUMP)
        {
            final E[] nextBuffer = nextBuffer(buffer, mask);
            return newBufferPoll(nextBuffer, cIndex);
        }

        // 将消费的元素从数组中移除
        // 更新取出的位置元素为null，注意是sp，不是so
        soRefElement(buffer, offset, null); // release element null
        // 修改consumerIndex的索引为新值，使用StoreStore屏障，直接更新到主内存
        soConsumerIndex(cIndex + 2); // release cIndex
        // 返回出队的元素
        return (E) e;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation is correct for single consumer thread use only.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E peek()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == null)
        {
            long pIndex = lvProducerIndex();
            // isEmpty?
            if ((cIndex - pIndex) / 2 == 0)
            {
                return null;
            }
            // peek() == null iff queue is empty, null element is not strong enough indicator, so we must
            // spin until element is visible.
            do
            {
                e = lvRefElement(buffer, offset);
            }
            while (e == null);
        }
        if (e == JUMP)
        {
            return newBufferPeek(nextBuffer(buffer, mask), cIndex);
        }
        return (E) e;
    }

    /**
     * We do not inline resize into this method because we do not resize on fill.
     */
    private int offerSlowPath(long mask, long pIndex, long producerLimit)
    {
        // 消费者索引，即consumerIndex
        final long cIndex = lvConsumerIndex();
        long bufferCapacity = getCurrentBufferCapacity(mask);

        // 消费索引+当前数组的容量 > 生产索引，代表当前数组已有部分元素被消费了
        // 此时不会扩容，会使用已被消费的槽位
        if (cIndex + bufferCapacity > pIndex)
        {
            // 修改producerLimit
            if (!casProducerLimit(producerLimit, cIndex + bufferCapacity))
            {
                // retry from top
                // CAS失败，自旋重试
                return RETRY;
            }
            else
            {
                // continue to pIndex CAS
                // CAS修改 生产索引
                return CONTINUE_TO_P_INDEX_CAS;
            }
        }
        // full and cannot grow
        // 根据生产者和消费者索引判断Queue是否已满，无界队列永不会满
        else if (availableInQueue(pIndex, cIndex) <= 0)
        {
            // offer should return false;
            return QUEUE_FULL;
        }
        // grab index for resize -> set lower bit
        // CAS的方式将producerIndex加1，奇数代表正在resize
        else if (casProducerIndex(pIndex, pIndex + 1))
        {
            // trigger a resize
            return QUEUE_RESIZE;
        }
        else
        {
            // failed resize attempt, retry from top
            return RETRY;
        }
    }

    /**
     * @return available elements in queue * 2
     */
    protected abstract long availableInQueue(long pIndex, long cIndex);

    @SuppressWarnings("unchecked")
    private E[] nextBuffer(final E[] buffer, final long mask)
    {
        final long offset = nextArrayOffset(mask);
        final E[] nextBuffer = (E[]) lvRefElement(buffer, offset);
        consumerBuffer = nextBuffer;
        consumerMask = (length(nextBuffer) - 2) << 1;
        soRefElement(buffer, offset, BUFFER_CONSUMED);
        return nextBuffer;
    }

    private static long nextArrayOffset(long mask)
    {
        return modifiedCalcCircularRefElementOffset(mask + 2, Long.MAX_VALUE);
    }

    private E newBufferPoll(E[] nextBuffer, long cIndex)
    {
        final long offset = modifiedCalcCircularRefElementOffset(cIndex, consumerMask);
        final E n = lvRefElement(nextBuffer, offset);
        if (n == null)
        {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        soRefElement(nextBuffer, offset, null);
        soConsumerIndex(cIndex + 2);
        return n;
    }

    private E newBufferPeek(E[] nextBuffer, long cIndex)
    {
        final long offset = modifiedCalcCircularRefElementOffset(cIndex, consumerMask);
        final E n = lvRefElement(nextBuffer, offset);
        if (null == n)
        {
            throw new IllegalStateException("new buffer must have at least one element");
        }
        return n;
    }

    @Override
    public long currentProducerIndex()
    {
        return lvProducerIndex() / 2;
    }

    @Override
    public long currentConsumerIndex()
    {
        return lvConsumerIndex() / 2;
    }

    @Override
    public abstract int capacity();

    @Override
    public boolean relaxedOffer(E e)
    {
        return offer(e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E relaxedPoll()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == null)
        {
            return null;
        }
        if (e == JUMP)
        {
            final E[] nextBuffer = nextBuffer(buffer, mask);
            return newBufferPoll(nextBuffer, cIndex);
        }
        soRefElement(buffer, offset, null);
        soConsumerIndex(cIndex + 2);
        return (E) e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E relaxedPeek()
    {
        final E[] buffer = consumerBuffer;
        final long cIndex = lpConsumerIndex();
        final long mask = consumerMask;

        final long offset = modifiedCalcCircularRefElementOffset(cIndex, mask);
        Object e = lvRefElement(buffer, offset);
        if (e == JUMP)
        {
            return newBufferPeek(nextBuffer(buffer, mask), cIndex);
        }
        return (E) e;
    }

    @Override
    public int fill(Supplier<E> s)
    {
        long result = 0;// result is a long because we want to have a safepoint check at regular intervals
        final int capacity = capacity();
        do
        {
            final int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
            if (filled == 0)
            {
                return (int) result;
            }
            result += filled;
        }
        while (result <= capacity);
        return (int) result;
    }

    @Override
    public int fill(Supplier<E> s, int limit)
    {
        if (null == s)
            throw new IllegalArgumentException("supplier is null");
        if (limit < 0)
            throw new IllegalArgumentException("limit is negative:" + limit);
        if (limit == 0)
            return 0;

        long mask;
        E[] buffer;
        long pIndex;
        int claimedSlots;
        while (true)
        {
            long producerLimit = lvProducerLimit();
            pIndex = lvProducerIndex();
            // lower bit is indicative of resize, if we see it we spin until it's cleared
            if ((pIndex & 1) == 1)
            {
                continue;
            }
            // pIndex is even (lower bit is 0) -> actual index is (pIndex >> 1)

            // NOTE: mask/buffer may get changed by resizing -> only use for array access after successful CAS.
            // Only by virtue offloading them between the lvProducerIndex and a successful casProducerIndex are they
            // safe to use.
            mask = this.producerMask;
            buffer = this.producerBuffer;
            // a successful CAS ties the ordering, lv(pIndex) -> [mask/buffer] -> cas(pIndex)

            // we want 'limit' slots, but will settle for whatever is visible to 'producerLimit'
            long batchIndex = Math.min(producerLimit, pIndex + 2l * limit); //  -> producerLimit >= batchIndex

            if (pIndex >= producerLimit)
            {
                int result = offerSlowPath(mask, pIndex, producerLimit);
                switch (result)
                {
                    case CONTINUE_TO_P_INDEX_CAS:
                        // offer slow path verifies only one slot ahead, we cannot rely on indication here
                    case RETRY:
                        continue;
                    case QUEUE_FULL:
                        return 0;
                    case QUEUE_RESIZE:
                        resize(mask, buffer, pIndex, null, s);
                        return 1;
                }
            }

            // claim limit slots at once
            if (casProducerIndex(pIndex, batchIndex))
            {
                claimedSlots = (int) ((batchIndex - pIndex) / 2);
                break;
            }
        }

        for (int i = 0; i < claimedSlots; i++)
        {
            final long offset = modifiedCalcCircularRefElementOffset(pIndex + 2l * i, mask);
            soRefElement(buffer, offset, s.get());
        }
        return claimedSlots;
    }

    @Override
    public void fill(Supplier<E> s, WaitStrategy wait, ExitCondition exit)
    {
        MessagePassingQueueUtil.fill(this, s, wait, exit);
    }
    @Override
    public int drain(Consumer<E> c)
    {
        return drain(c, capacity());
    }

    @Override
    public int drain(Consumer<E> c, int limit)
    {
        return MessagePassingQueueUtil.drain(this, c, limit);
    }

    @Override
    public void drain(Consumer<E> c, WaitStrategy wait, ExitCondition exit)
    {
        MessagePassingQueueUtil.drain(this, c, wait, exit);
    }

    /**
     * Get an iterator for this queue. This method is thread safe.
     * <p>
     * The iterator provides a best-effort snapshot of the elements in the queue.
     * The returned iterator is not guaranteed to return elements in queue order,
     * and races with the consumer thread may cause gaps in the sequence of returned elements.
     * Like {link #relaxedPoll}, the iterator may not immediately return newly inserted elements.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new WeakIterator(consumerBuffer, lvConsumerIndex(), lvProducerIndex());
    }

    private static class WeakIterator<E> implements Iterator<E>
    {
        private final long pIndex;
        private long nextIndex;
        private E nextElement;
        private E[] currentBuffer;
        private int mask;

        WeakIterator(E[] currentBuffer, long cIndex, long pIndex)
        {
            this.pIndex = pIndex >> 1;
            this.nextIndex = cIndex >> 1;
            setBuffer(currentBuffer);
            nextElement = getNext();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }

        @Override
        public boolean hasNext()
        {
            return nextElement != null;
        }

        @Override
        public E next()
        {
            final E e = nextElement;
            if (e == null)
            {
                throw new NoSuchElementException();
            }
            nextElement = getNext();
            return e;
        }

        private void setBuffer(E[] buffer)
        {
            this.currentBuffer = buffer;
            this.mask = length(buffer) - 2;
        }

        private E getNext()
        {
            while (nextIndex < pIndex)
            {
                long index = nextIndex++;
                E e = lvRefElement(currentBuffer, calcCircularRefElementOffset(index, mask));
                // skip removed/not yet visible elements
                if (e == null)
                {
                    continue;
                }

                // not null && not JUMP -> found next element
                if (e != JUMP)
                {
                    return e;
                }

                // need to jump to the next buffer
                int nextBufferIndex = mask + 1;
                Object nextBuffer = lvRefElement(currentBuffer,
                                              calcRefElementOffset(nextBufferIndex));

                if (nextBuffer == BUFFER_CONSUMED || nextBuffer == null)
                {
                    // Consumer may have passed us, or the next buffer is not visible yet: drop out early
                    return null;
                }

                setBuffer((E[]) nextBuffer);
                // now with the new array retry the load, it can't be a JUMP, but we need to repeat same index
                e = lvRefElement(currentBuffer, calcCircularRefElementOffset(index, mask));
                // skip removed/not yet visible elements
                if (e == null)
                {
                    continue;
                }
                else
                {
                    return e;
                }

            }
            return null;
        }
    }

    private void resize(long oldMask, E[] oldBuffer, long pIndex, E e, Supplier<E> s)
    {
        assert (e != null && s == null) || (e == null || s != null);
        // 下一个Buffer的长度，MpscQueue会构建一个相同长度的Buffer
        int newBufferLength = getNextBufferSize(oldBuffer);
        final E[] newBuffer;
        try
        {
            // 创建一个新的E[]
            newBuffer = allocateRefArray(newBufferLength);
        }
        catch (OutOfMemoryError oom)
        {
            assert lvProducerIndex() == pIndex + 1;
            soProducerIndex(pIndex);
            throw oom;
        }

        // 生产者Buffer指向新的E[]
        producerBuffer = newBuffer;
        // 计算新的Mask，Buffer长度不变的情况下，Mask也不变
        final int newMask = (newBufferLength - 2) << 1;
        producerMask = newMask;

        // JUMP对象所放的位置 , 计算pIndex在旧数组的偏移
        final long offsetInOld = modifiedCalcCircularRefElementOffset(pIndex, oldMask);
        // 计算pIndex在新数组的偏移，添加到队列的元素放在新数组的位置，大家发现一个规率没有
        // 如果新旧数组长度一样时，JUMP在旧数组的索引位置和 元素放到新数组的
        // 索引位置相等，这一点需要注意，在看poll()代码时需要用到
        final long offsetInNew = modifiedCalcCircularRefElementOffset(pIndex, newMask);

        // 都是CAS操作，设置新加入的元素到新数组
        soRefElement(newBuffer, offsetInNew, e == null ? s.get() : e);// element in new array
        // 旧数组的最后一个位置指向新的数组，将指向新数组的指针存储于旧数组的length-1的索引位置
        soRefElement(oldBuffer, nextArrayOffset(oldMask), newBuffer);// buffer linked

        // ASSERT code
        final long cIndex = lvConsumerIndex();
        // 这里需要注意 ，maxQueueCapacity的初始值为 maxCapacity * 2，而pIndex、cIndex也是以2为单位递增的
        final long availableInQueue = availableInQueue(pIndex, cIndex);
        RangeUtil.checkPositive(availableInQueue, "availableInQueue");

        // Invalidate racing CASs
        // 更新limit
        // We never set the limit beyond the bounds of a buffer
        // 取newMask和availableInQueue的最小值 加上 pIndex，因为后面也是会进行取余取下标的
        soProducerLimit(pIndex + Math.min(newMask, availableInQueue));

        // make resize visible to the other producers
        // 更新pIndex
        soProducerIndex(pIndex + 2);

        // INDEX visible before ELEMENT, consistent with consumer expectation

        // make resize visible to consumer
        // pIndex在旧数组的位置设置一个固定值-JUMP，来告诉要跳到下一个数组
        // 将JUMP 存储在旧数组的offsetInOld位置，当消费者查找到元素为
        // JUMP，证明数组已经扩容，则根据next数组指针找到下一个数组
        // 在next数组相同的位置的元素，就是本次要消费的元素
        soRefElement(oldBuffer, offsetInOld, JUMP);
    }

    /**
     * @return next buffer size(inclusive of next array pointer)
     */
    protected abstract int getNextBufferSize(E[] buffer);

    /**
     * @return current buffer capacity for elements (excluding next pointer and jump entry) * 2
     */
    protected abstract long getCurrentBufferCapacity(long mask);
}
