package gogirl_user;
//public class SegmentLockTest implements Runnable {
//	SegmentLock segmentLock = SegmentLock.getInsatance();//入参两个锁（两个并发），公平锁
//	private static int  i = 0;
//	@Override
//	public void run() {
////		System.out.println("segmentLock:"+segmentLock.toString());
//		for(int j=0;j<10;j++){
//			int id= (int)(Math.random()*100);
//			try {
//				segmentLock.lock(id);
//				try {
//					Thread.currentThread().sleep(500);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				System.out.println(i++);
//				segmentLock.unlock(id);
//			} catch (Exception e) {
//				System.out.println("lock异常，不执行unlock***************");
//			}
//			
//
//		}
//	}
//	
//	public static void main(String[] args) throws InterruptedException {
//		for(int i=0;i<100;i++){
//			SegmentLockTest test = new SegmentLockTest();
//			Thread t = new Thread(test,"t"+i);
//			t.start();
//		}
//	}
//
//
//
//
//
//}
