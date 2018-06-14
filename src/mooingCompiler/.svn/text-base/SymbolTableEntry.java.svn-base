package mooingCompiler;




public class SymbolTableEntry {

	
	String shape="n/a";
	String rows="n/a";
	String columns="n/a";
	String type="n/a";
	String callby="n/a";
	Boolean initialized=false;
	
	public SymbolTableEntry() {
		super();
	}

	public SymbolTableEntry (SymbolTableEntry clone) {
		this.shape = clone.getShape();
		this.rows = clone.getRows();
		this.columns = clone.getColumns();
		this.type = clone.getType();
	}
	
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "type=" + type + " shape=" + shape + ", rows=" + rows
				+ ", columns=" + columns + ", callby=" + callby;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getType() {
		return type;
	}

	public void setType(String numType) {
		this.type = numType;
	}

	public Boolean getInitialized() {
		return initialized;
	}

	public void setInitialized(Boolean initialized) {
		this.initialized = initialized;
	}
	
	public String getCallby() {
		return callby;
	}

	public void setCallby(String callby) {
		this.callby = callby;
	}
	
	
}
