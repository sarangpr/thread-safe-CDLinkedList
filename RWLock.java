/* Name: Sarang Kunte
 * Email id: sarangpr@buffalo.edu
 */
package rwlist;
// This class is used to implement RW Locks
import java.util.concurrent.atomic.AtomicInteger;


public class RWLock {
	
	Object lock = new Object();
	AtomicInteger lockState=new AtomicInteger(1);
	
	void lockRead(){
		int n=lockState.get();
		if(n>=1){//increment the lockState if it is unlocked or in read state.
			while(!lockState.compareAndSet(n, n+1)){
				n=lockState.get();
			}
		}
		else{
			synchronized(lock){
				try {//if the lockState isnt postive wait for the writer to finish execution.
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lockRead();//after waiting again see if it can obtain the lock.
		}
	}
	
	void unlockRead(){
		int n=lockState.get();
		if(n<(-1)){//increment lockState if lockState is negative
			while(!lockState.compareAndSet(n, n+1)){
				n=lockState.get();
			}
		}else if(n>1){//decrement the lockState if lockState is positive
			while(!lockState.compareAndSet(n, n-1)){
				n=lockState.get();
			}
		}
		synchronized(lock){
			lock.notify();//wake up the first waiting thread
		}
	}
	
	void lockWrite(){
		int n=lockState.get();
		if(n>1){
			while(!lockState.compareAndSet(n,-n)){ //if currently a reads have the lock make it negative 
				n=lockState.get();
			}
		}else if(n==1){
			while(!lockState.compareAndSet(1, 0));//if unlocked lock it
		}else if(n==(-1)){
			while(!lockState.compareAndSet(-1, 0));//if unlocked lock it
		}else{
			synchronized(lock){
				try {
					lock.wait();//if lock not obtained wait
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			lockWrite();//try to gain the lock again after waiting
		}
	}
	
	void unlockWrite(){
		while(!lockState.compareAndSet(0,1));
		synchronized(lock){
			lock.notify();//wake up the first thread that called wait on the lock object.
		}
	}
}
