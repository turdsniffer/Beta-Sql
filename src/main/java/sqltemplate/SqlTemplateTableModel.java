package sqltemplate;

import com.google.common.collect.Lists;
import com.swingautocompletion.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;

public class SqlTemplateTableModel extends AbstractTableModel
{
    private List<Pair<String,String>> templates = Lists.newArrayList();
    
    public void setData(Map<String,String> templates)
    {
        this.templates = templates.entrySet().stream().map(e -> new Pair<String, String>(e.getKey(), e.getValue())).collect(Collectors.toList());
        this.fireTableDataChanged();
    }
    
    @Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
        Pair<String, String> templatePair = templates.get(rowIndex);
		if(columnIndex == 0)
			return templatePair.getFirst();
        else
            return templatePair.getSecond();
	}

    public Pair<String,String> getTemplateForRow(int rowId)
    {
        return templates.get(rowId);
    }


	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public int getRowCount()
	{
		return templates.size();
	}

	@Override
	public String getColumnName(int column)
	{
		      switch (column)
        {
            case 1:
                return "Name";
            default:
                return "Template";
        }
	}
    
    
}
