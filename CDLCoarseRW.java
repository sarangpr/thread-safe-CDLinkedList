/* Name: Sarang Kunte
 * Email id: sarangpr@buffalo.edu
 */
package rwlist;

public class CDLCoarseRW<T> extends CDLListRW<T> {
	public CDLCoarseRW(T v) {
		super(v);
		
	}

	public Cursor reader(Element from){ 
		Cursor c = new Cursor(from);
		return c;
		
	}
	public class Cursor extends CDLListRW<T>.Cursor{
		public Cursor(Element head){
			super(head);
		}
		public void next(){
			coarseLock.lockRead();
			curr=curr.next;
			coarseLock.unlockRead();
		}
		public void previous(){
			coarseLock.lockRead();
			curr=curr.prev;
			coarseLock.unlockRead();
		}
		public Writer writer(){
			Writer w=new Writer(curr);
			return w;
		}
		
	}
	public class Writer extends CDLListRW<T>.Writer{

		public Writer(Element curr) {
			super(curr);
		}
		public boolean insertAfter(T val){
				coarseLock.lockWrite();
				super.insertAfter(val);
				coarseLock.unlockWrite();
			
			return true;
		}
		public boolean insertBefore(T val){
			
				coarseLock.lockWrite();
				super.insertBefore(val);
				coarseLock.unlockWrite();
			
			return true;
		}
		public void delete(Cursor c){
				coarseLock.lockWrite();
				super.delete(c);
				coarseLock.unlockWrite();
			
		}
		
	}
	
}
