package base.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡算法
 *  RoundLoaderBalancer 轮旋算法
 */
public class Balance{
    private static final List<String> SERVICE_LIST= new ArrayList<>();
    
    static {
        SERVICE_LIST.add("192.168.78.1");
        SERVICE_LIST.add("192.168.78.2");
        SERVICE_LIST.add("192.168.78.3");
        SERVICE_LIST.add("192.168.78.4");
        SERVICE_LIST.add("192.168.78.5");
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(new RoundLoaderBalancer().getService());
        }
    }

    /**
     * 负载均衡接口
     */
    private interface Balancer{
        String getService();
    }

    /**
     * 轮询算法
     */
    private static class RoundLoaderBalancer implements Balancer{

        private static AtomicInteger idx = new AtomicInteger(0);

        @Override
        public String getService() {
            int index = getNextNonNegative();
            for (int i = 0; i < SERVICE_LIST.size(); i++) {
                //获取服务
                String service = SERVICE_LIST.get((i + index) % SERVICE_LIST.size());
                //判断服务是否可用
                return service;
            }
            return null;
        }

        private int getNextNonNegative() {
            return getNonNegative(idx.incrementAndGet());
        }
        /**
         * 通过二进制位操作将originValue转化为非负数:
         * 0和正数返回本身
         * 负数通过二进制首位取反转化为正数或0（Integer.MIN_VALUE将转换为0）
         * return non-negative int value of originValue
         *
         * @param originValue
         * @return positive int
         */
        public int getNonNegative(int originValue) {
            return 0x7fffffff & originValue;
        }
    }
}