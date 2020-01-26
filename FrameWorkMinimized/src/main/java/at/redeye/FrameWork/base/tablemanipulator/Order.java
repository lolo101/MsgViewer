package at.redeye.FrameWork.base.tablemanipulator;

class Order
{
    String name;
    int position_now;
    int position_wanted;

    public Order(String name, int position_now, int position_wanted )
    {
        this.name = name;
        this.position_now = position_now;
        this.position_wanted = position_wanted;
    }

    @Override
    public String toString()
    {
        return String.format("%d soll %d %s", position_now, position_wanted, name );
    }

    boolean isOnWantedPosition()
    {
        if( position_wanted == -1 )
            return true;

        return position_now == position_wanted;
    }
}
