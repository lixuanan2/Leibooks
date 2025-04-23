package leibooks.domain.facade.events;

public class RemoveShelfEvent extends AShelfEvent {
	
	public RemoveShelfEvent (String name) {
		super (name);
	}
	
	@Override
	public String toString() {
		return "RemoveShelfEvent [shelfName=" + getName() + "]";
	}
}
