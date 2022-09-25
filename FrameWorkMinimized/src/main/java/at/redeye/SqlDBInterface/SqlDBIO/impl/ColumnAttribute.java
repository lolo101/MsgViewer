package at.redeye.SqlDBInterface.SqlDBIO.impl;

import at.redeye.FrameWork.base.bindtypes.DBEnum;
import at.redeye.FrameWork.base.bindtypes.DBString;
import at.redeye.FrameWork.base.bindtypes.DBValue;

public class ColumnAttribute {

    private final boolean primaryKey;

    private final boolean hasIndex;

    private final DBDataType datatype;

    private final int width;

    public ColumnAttribute(DBValue val) {
        this.datatype = val.getDBType();
        this.primaryKey = val.isPrimaryKey();
        this.hasIndex = val.shouldHaveIndex();
        if (val instanceof DBString) {
            this.width = ((DBString) val).getMaxLen();
        } else if (val instanceof DBEnum) {
            this.width = ((DBEnum<?>) val).getMaxLen();
        } else {
            this.width = 0;
        }
    }

    public int getWidth() {
        return width;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.primaryKey ? 1 : 0);
        hash = 67 * hash + (this.hasIndex ? 1 : 0);
        hash = 67 * hash + (this.datatype != null ? this.datatype.hashCode() : 0);
        hash = 67 * hash + this.width;
        return hash;
    }

    @Override
    public boolean equals( Object other )
    {
        if( other == null )
            return false;

        if( other == this )
            return true;

        if( other instanceof ColumnAttribute ) {
            ColumnAttribute attr = (ColumnAttribute) other;

            if (this.hasIndex != attr.hasIndex)
                return false;

            if (this.primaryKey != attr.primaryKey)
                return false;

            if (this.width != attr.width)
                return false;

            return this.datatype == attr.datatype;
        }
        return false;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(datatype);

        if (width > 0) {
            sb.append("(");
            sb.append(width);
            sb.append(")");
        }

        if (this.primaryKey)
            sb.append(" Primary");

        if (this.hasIndex)
            sb.append(" Index");

        return sb.toString();
    }

}
