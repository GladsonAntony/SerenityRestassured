package dataprovider;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class XlsxSheetDataProvider
{
    // The transient keyword in Java is used to indicate that a field should not be serialized.
    private transient Collection data = null;
    private static XSSFSheet xlsxWorkSheet;
    private static XSSFWorkbook xlsxWorkBook;
    private static XSSFCell xlsxCell;
    private static XSSFRow xlsxRow;

    private Collection loadFromSpreadsheet(final InputStream excelFile) throws Exception
    {
        xlsxWorkBook = new XSSFWorkbook(excelFile);

        data = new ArrayList();
        xlsxWorkSheet = xlsxWorkBook.getSheetAt(0);

        int numberOfColumns = countNonEmptyColumns(xlsxWorkSheet);
        List rows = new ArrayList();
        List rowData = new ArrayList();

        for (Row row : xlsxWorkSheet)
        {
            if (isEmpty(row))
            {
                break;
            }
            else
            {
                rowData.clear();
                for (int column = 0; column < numberOfColumns; column++)
                {
                    Cell cell= row.getCell(column);
                    rowData.add(objectFrom(xlsxWorkBook, cell));
                }
                rows.add(rowData.toArray());
            }
        }
        return rows;
    }

    public  XlsxSheetDataProvider(final InputStream excelInputStream) throws Exception
    {
        this.data = loadFromSpreadsheet(excelInputStream);
    }

    public Collection getData()
    {
        return data;
    }

    private boolean isEmpty(final Row row)
    {
        Cell firstCell = row.getCell(1);
        boolean rowIsEmpty = (firstCell == null)
                || (firstCell.getCellType() == Cell.CELL_TYPE_BLANK);
        return rowIsEmpty;
    }

    /**
     * Count the number of columns, using the number of non-empty cells in the
     * first row.
     */
    private int countNonEmptyColumns(final Sheet sheet)
    {
        Row firstRow = sheet.getRow(1);
        return firstEmptyCellPosition(firstRow);
    }

    private int firstEmptyCellPosition(final Row cells)
    {
        int columnCount = 0;
        for (Cell cell : cells) {
            if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
            {
                break;
            }
            columnCount++;
        }
        return columnCount;
    }

    private Object objectFrom(final XSSFWorkbook xlsxWorkBook, final Cell cell)
    {
        Object cellValue = null;

        if (cell.getCellType() == Cell.CELL_TYPE_STRING)
        {
            cellValue = cell.getRichStringCellValue().getString();
        }
        else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            cellValue = getNumericCellValue(cell);
        }
        else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
        {
            cellValue = cell.getBooleanCellValue();
        }
        else if (cell.getCellType()  ==Cell.CELL_TYPE_FORMULA) {
            cellValue = evaluateCellFormula(xlsxWorkBook, cell);
        }

        return cellValue;

    }

    private Object getNumericCellValue(final Cell cell)
    {
        Object cellValue;
        if (DateUtil.isCellDateFormatted(cell))
        {
            cellValue = new Date(cell.getDateCellValue().getTime());
        }
        else
        {
            cellValue = cell.getNumericCellValue();
        }
        return cellValue;
    }

    public static Object evaluateCellFormula(XSSFWorkbook xlsxWorkBook, final Cell cell)
    {
        FormulaEvaluator evaluator = xlsxWorkBook.getCreationHelper()
                .createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        Object result = null;

        if (cellValue.getCellType() == Cell.CELL_TYPE_BOOLEAN)
        {
            result = cellValue.getBooleanValue();
        }
        else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC)
        {
            result = cellValue.getNumberValue();
        }
        else if (cellValue.getCellType() == Cell.CELL_TYPE_STRING)
        {
            result = cellValue.getStringValue();
        }
        return result;
    }



}
