//class for items in shop
public class ItemClass
{
	String item_name;
	double item_price;
	int item_quantity;
	public ItemClass(String name,double price)
	{
		item_name=name;
		item_price=price;
	}
	String toStr()
	{
		return "/"+item_name+","+item_price+","+item_quantity;
	}


}