package by.barsnik96.HeadHunter_JSP.utils;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseListModel extends AbstractListModel
{
    private ArrayList<String> data = new ArrayList<String>();

    public void setDataSource(ResultSet result_set, String column) throws SQLException
    {
        data.clear();
        while (result_set.next())
        {
            data.add(result_set.getString(column));
        }
        fireIntervalAdded(this, 0, data.size());
    }

    public int getSize()
    {
        return data.size();
    }

    public Object getElementAt(int index)
    {
        return data.get(index);
    }
}
