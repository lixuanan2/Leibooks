package leibooks.domain.facade.events;

public class AShelfEvent implements ShelfEvent {
	private final String name;

	public AShelfEvent(String name) {
		this.name = name;
	}

	@Override
	public String getShelfName() {
		return getName();
	}

	public String getName() {
		return name;
	}
}
