package ui;

@await
class AbstractForm<T> extends AbstractWindow {
	@async
	public function execute(): T {
		create();
		return null;
	}
}

