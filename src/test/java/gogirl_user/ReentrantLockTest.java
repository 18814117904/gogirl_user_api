package gogirl_user;
/*了解ReentrantLock锁*/
import java.util.concurrent.locks.ReentrantLock;
//
//public class ReentrantLockTest implements Runnable{
//    public ReentrantLock lock = new ReentrantLock(false);
//    public int i = 0;
//
//    @Override
//    public void run() {
//        for (int j = 0; j < 100; j++) {
//            lock.lock();  // 看这里就可以
//            //lock.lock(); //①
//            try {
//                System.out.println(Thread.currentThread().getName()+i++);
//                try {
//					Thread.currentThread().sleep(400);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            } finally {
//                lock.unlock(); // 看这里就可以
//                //lock.unlock();//②
//            }
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        ReentrantLockTest test = new ReentrantLockTest();
//        Thread t1 = new Thread(test,"aa");
//        Thread t2 = new Thread(test,"bb");
////        System.err.println(i);
////        ReentrantLockTest test2 = new ReentrantLockTest();
////        Thread t3 = new Thread(test2,"cc");
////        Thread t4 = new Thread(test2,"dd");
//        t1.start();t2.start();
////        t3.start();t4.start();
//        t1.join(); t2.join(); // main线程会等待t1和t2都运行完再执行以后的流程
////        t3.join(); t4.join(); // main线程会等待t1和t2都运行完再执行以后的流程
////        System.err.println(i);
//    }
//}