package io.jenkins.plugins.reporter.model;

import io.jenkins.plugins.datatables.TableColumn;
import io.jenkins.plugins.reporter.ItemViewModel;
import org.apache.commons.text.CaseUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides the model for the item table. The model displays the distribution for the subitems and the id column is 
 * linked to the {@link ItemViewModel} of the selected subitem.
 *
 * @author Simon Symhoven
 */
public class ItemTableModel {
    
    private final Report report;
    
    private final Item item;

    /**
     * Creates a new instance of {@link ItemTableModel}.
     * 
     * @param report
     *         the report with result
     *         
     * @param item
     *         the item to render
     */
    public ItemTableModel(final Report report, final Item item) {
        super();
        
        this.report = report;
        this.item = item;
    }
    
    public String getId() {
        return item.getId();
    }
    
    public Report getReport() {
        return report;
    }
    
    public Item getItem() {
        return item;
    }
    
    public List<TableColumn> getColumns() {
        List<TableColumn> columns = new ArrayList<>();
        item.getResult().keySet().forEach(property -> columns.add(createResultAbsoluteColumn(property)));
        return columns;
    }
    
    public List<ItemRow> getRows() {
        return item.getItems()
            .stream()
            .map(item -> new ItemRow(report, item))
            .collect(Collectors.toList());
    }

    protected TableColumn createResultAbsoluteColumn(String property) {
        return new TableColumn.ColumnBuilder()
                .withDataPropertyKey(String.format("%s-absolute", property))
                .withHeaderLabel(CaseUtils.toCamelCase(property, true))
                .withHeaderClass(TableColumn.ColumnCss.NUMBER)
                .build();
    }

    public String label(Integer value) {
        return item.getLabel(report, value);
    }

    /**
     * A table row that shows the properties of an item.
     */
    public static class ItemRow {
        
        private final Report report;
        private final Item item;
        

        /**
         * Creates a new instance of {@link ItemRow}.
         * 
         * @param report
         *          the report with the result.
         *          
         * @param item
         *          the item to render.
         */
        ItemRow(Report report, Item item) {
            this.report = report;
            this.item = item;
        }
        
        public String getId() {
            return item.getId();
        }
        
        public String getName() {
            return item.getName();
        }
        
        public Item getItem() {
            return item;
        }
        
        public Map<String, String> getColors() {
            return report.getResult().getColors();
        }

        public String label(Integer value) {
            return item.getLabel(report, value);
        }
        
        public String tooltip(String id, double percentage) {
            return String.format("%s: %.2f%%", id, percentage);
        }
    }
}
