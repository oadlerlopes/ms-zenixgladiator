package br.com.zenix.gladiator.managers.managements.hologram;

public class PlayerTop {

	private String name;
	private Integer id;
	private int top;

	public PlayerTop(Integer id, String name, int top) {
		this.id = id;
		this.name = name;
		this.top = top;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getTop() {
		return top;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public static class PlayerTopLoses {

		private String name;
		private Integer id;
		private int top;

		public PlayerTopLoses(Integer id, String name, int top) {
			this.id = id;
			this.name = name;
			this.top = top;
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public int getTop() {
			return top;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setTop(int top) {
			this.top = top;
		}
	}

	public static class PlayerTopMKS {

		private String name;
		private Integer id;
		private int top;

		public PlayerTopMKS(Integer id, String name, int top) {
			this.id = id;
			this.name = name;
			this.top = top;
		}

		public Integer getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public int getTop() {
			return top;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setTop(int top) {
			this.top = top;
		}
	}
}
