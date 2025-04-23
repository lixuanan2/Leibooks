package leibooks.domain.facade.events;


public class AddShelfEvent extends AShelfEvent {

	public AddShelfEvent(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "AddShelfEvent [shelfName=" + getName() + "]";
	}
	
}
