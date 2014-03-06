/* Name: Sarang Kunte
 * Email id: sarangpr@buffalo.edu
 */
package rwlist;


public class CDLListRW<T> {
	RWLock coarseLock=new RWLock();//Used in Coarse Locking, one lock for the entire list.
	Element head= new Element();
	
	public CDLListRW(T v){
		head.data=v;
		head.next=head;
		head.prev=head;
	}
	public class Element{
		T data; 
		Element prev, next;
		RWLock fineLock = new RWLock();//Used in Fine locking, one lock per element.
		public T value(){ 
			return this.data;
		}
	}
	public Element head(){return head;}

	public Cursor reader(Element from){ 
		Cursor c = new Cursor(from);
		return c;
		
	}
	
	public class Cursor{
		
		Element curr;
		
		public Cursor(){}
		
		public Cursor(Element head){
			curr=head;
		}
			
		public void printList(String t){
			Element temp=head;
			do{
			System.out.println(t+":"+temp.data);
			//next();
			temp=temp.next;
			}
			while(temp.data!=head.data);
		}
		public Element current(){return curr;}
		
		public void previous(){curr=curr.prev;}
		
		public void next(){curr=curr.next;}
		
		public Writer writer(){
			Writer w=new Writer(curr);
			return w;
		}
	}
	public class Writer {
		Element wcurr;
		Element temp=new Element();
		public Writer(Element curr){
			wcurr=curr;
		}
		
		public boolean insertBefore(T val){
			temp.data=val;
			wcurr.prev.next=temp;
			temp.prev=wcurr.prev;
			wcurr.prev=temp;
			temp.next=wcurr;
			return true;
		}
		
		public boolean insertAfter(T val){
			temp.data=val;
			wcurr.next.prev=temp;
			temp.next=wcurr.next;
			wcurr.next=temp;
			temp.prev=wcurr;
			return true;
		}
		public boolean delete(Cursor c){
			if(wcurr==head)
				return false;
			Element t=wcurr.next;
			wcurr.prev.next=wcurr.next;
			wcurr.next.prev=wcurr.prev;
			wcurr.next=null;
			wcurr.prev=null;
			wcurr.data=null;
			c.curr=t;
			return true;
		}
	}
	public void printList(String t){
		Element temp=head;
		do{
			System.out.println(t+temp.value());
			temp=temp.next;
		}while(temp!=head);
	}
}
