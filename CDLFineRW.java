/* Name: Sarang Kunte
 * Email id: sarangpr@buffalo.edu
 */
package rwlist;


public class CDLFineRW<T> extends CDLListRW<T> {
	Element ghost=new Element();

	public CDLFineRW(T v) {
		super(v);
		ghost.data=null;
		head.prev=ghost;
		head.next=ghost;
		ghost.next=head;
		ghost.prev=head;
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
			Element t=curr;
			t.fineLock.lockRead();
			
			t.next.fineLock.lockRead();
			
			curr=curr.next;
			if(curr==ghost)
			{
				next();
			}
			t.next.fineLock.unlockRead();
			
			t.fineLock.unlockRead();
			
		}
		
		public void previous(){
			Element t=curr;
			t.prev.fineLock.lockRead();
			
			t.fineLock.lockRead();
			
			curr=curr.prev;
			if(curr==ghost)
			{	
				previous();
			}
			t.fineLock.unlockRead();
			
			t.prev.fineLock.unlockRead();
			
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
		public boolean insertAfter(T val, Cursor c){
			c.current().fineLock.lockWrite();
			c.current().next.fineLock.lockWrite();
			insertAfter(val);
			c.current().next.fineLock.unlockWrite();
			c.current().fineLock.unlockWrite();
			return true;
			
		}
		
		public boolean insertBefore(T val,Cursor c){
			if(c.current()==head&&c.current().next==ghost){
				insertAfter(val,c);
			}
			else if(c.current()==head&&c.current().next!=ghost){
						c.previous();
						insertAfter(val,c);
			}
			else{
						c.current().prev.fineLock.lockWrite();
						c.current().fineLock.lockWrite();
						insertBefore(val);
						c.current().fineLock.unlockWrite();
						c.current().prev.fineLock.unlockWrite();
						
			}
			return true;
		}
		public boolean delete(Cursor c){
			c.current().prev.fineLock.lockWrite();
			c.current().fineLock.lockWrite();
			c.current().next.fineLock.lockWrite();
			super.delete(c);
			c.current().next.fineLock.unlockWrite();
			c.current().fineLock.unlockWrite();
			c.current().prev.fineLock.unlockWrite();
			return true;
		}
	}
	public void printList(String t){
		Element temp=head;
		do{
			if(temp!=ghost)
				System.out.println(t+":"+temp.data);
			temp=temp.next;
		}
		while(temp.data!=head.data);
	}
}