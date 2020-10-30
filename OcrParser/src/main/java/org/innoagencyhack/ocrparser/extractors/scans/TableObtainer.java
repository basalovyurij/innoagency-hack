package org.innoagencyhack.ocrparser.extractors.scans;

import org.innoagencyhack.ocrparser.extractors.scans.models.MatOfPointComparator;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.innoagencyhack.ocrparser.models.Cell;
import org.innoagencyhack.ocrparser.extractors.scans.models.ImageKernelContainer;
import org.innoagencyhack.ocrparser.extractors.scans.models.ImgCell;
import org.innoagencyhack.ocrparser.models.TableResponse;
import org.innoagencyhack.ocrparser.models.TableResponseCell;
import org.innoagencyhack.ocrparser.extractors.scans.models.ScanTableCell;
import org.innoagencyhack.ocrparser.extractors.scans.tesseract.TesseractOcr;

public class TableObtainer {

    private final TesseractOcr tesseract;
    private final int maxAspectRatio = 50;
    private final int delta = 15;

    public TableObtainer(TesseractOcr tesseract) {
        this.tesseract = tesseract;
    }
    
    private boolean intersectionChecker(int x, int y, List<MatOfPoint> contours) {
        for (MatOfPoint contour : contours) {
            if (!intersectionChecker(x, y, contour))
                return false;
        }
        return true;
    }

    private boolean intersectionChecker(int x, int y, MatOfPoint contour) {
        Rect contourRect = Imgproc.boundingRect(contour);
        boolean doesNotIntersect = x < contourRect.x || x > contourRect.x + contourRect.width
                || y < contourRect.y || y > contourRect.y + contourRect.height;
        return doesNotIntersect;
    }

    private boolean checkIntersections(MatOfPoint contour, List<MatOfPoint> tables) {
        Rect boundingRect = Imgproc.boundingRect(contour);
        boolean noIntersections = tables.stream().allMatch(x -> intersectionChecker(boundingRect.x, boundingRect.y, tables));
        boolean isCorrectContour = boundingRect.width / boundingRect.height < maxAspectRatio;
        boolean tablesIsEmpty = tables.isEmpty();
        return (noIntersections || tablesIsEmpty) && isCorrectContour;
    }

    private List<MatOfPoint> getTableContours(List<MatOfPoint> contours) {
        List<MatOfPoint> tables = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            if (checkIntersections(contour, tables)) {
                tables.add(contour);
            }
        }
        return tables;
    }

    private boolean isTableCell(MatOfPoint contour, MatOfPoint table) {
        Rect contourRect = Imgproc.boundingRect(contour);
        double contourArea = Imgproc.contourArea(contour);
        double tableArea = Imgproc.contourArea(table);
        boolean isSameContour = tableArea == contourArea;

        return !intersectionChecker(contourRect.x, contourRect.y, table) && !isSameContour;
    }

    private List<ImgCell> getTableCells(MatOfPoint table, List<MatOfPoint> contours, Mat originalImage) {
        List<MatOfPoint> filtered = contours.stream().filter(x -> isTableCell(x, table)).collect(Collectors.toList());
        List<ImgCell> cells = filtered.stream().map(x -> ImageHelper.getOriginalFragment(x, originalImage)).collect(Collectors.toList());
        return cells;
    }

    public List<List<ImgCell>> getTablesWithCells(Mat image) {
        List<List<ImgCell>> tables = new ArrayList<>();
        Mat treshImg = ImageHelper.getReversedTresholdImage(image);
        ImageKernelContainer kernels = ImageHelper.getKernels(image);
        Mat contoursImage = ImageHelper.getContoursImage(treshImg, kernels);
        Mat correctTreshImg = ImageHelper.getReversedTresholdImage(contoursImage);
        List<MatOfPoint> contours = ImageHelper.getContours(correctTreshImg);
        if(contours.size() < 2)
            return tables;
        
        contours.sort(new MatOfPointComparator());
        List<MatOfPoint> contoursWithoutWholePage = contours.subList(1, contours.size() - 1);
        List<MatOfPoint> tableContours = getTableContours(contoursWithoutWholePage);
        for (MatOfPoint table : tableContours) {
            List<ImgCell> cells = getTableCells(table, contoursWithoutWholePage, image);
            if (cells.size() > 0) {
                tables.add(cells);
            }
        }

        return tables;
    }

    public Integer getRowKey(ScanTableCell cell, HashMap<Integer, List<ScanTableCell>> rows) {
        if (rows.size() > 0) {
            for (Map.Entry<Integer, List<ScanTableCell>> row : rows.entrySet()) {
                Integer key = row.getKey();
                if (key + delta >= cell.y && cell.y >= key - delta) {
                    return key;
                }
            }
        }

        return null;
    }

    public HashMap<Integer, List<ScanTableCell>> getRows(List<ImgCell> cells) {
        HashMap<Integer, List<ScanTableCell>> rows = new HashMap<>();
        List<ScanTableCell> cellContours = cells.stream().map(c
                -> new ScanTableCell(Imgproc.boundingRect(c.Contour), c.Image)).collect(Collectors.toList());
        for (ScanTableCell cell : cellContours) {
            Integer key = getRowKey(cell, rows);
            if (key == null) {
                List<ScanTableCell> row = new ArrayList<>();
                row.add(cell);
                rows.put(cell.y, row);
            } else {
                rows.get(key).add(cell);
            }
        }
        return rows;
    }

    public Map<Integer, List<ScanTableCell>> getSortedRows(List<ImgCell> cells) {
        HashMap<Integer, List<ScanTableCell>> rows = getRows(cells);
        for (Map.Entry<Integer, List<ScanTableCell>> row : rows.entrySet()) {
            Integer key = row.getKey();
            List<ScanTableCell> values = row.getValue();
            values.sort(new ScanTableCell.ColumnComparator());
            rows.put(key, values);
        }
        Map<Integer, List<ScanTableCell>> treeMap = new TreeMap<>(rows);
        return treeMap;
    }

    public List<ScanTableCell> getPatternRow(Map<Integer, List<ScanTableCell>> rows) {
        int resultRowLen = 0;
        List<ScanTableCell> patternRow = new ArrayList<>();

        for (Map.Entry<Integer, List<ScanTableCell>> row : rows.entrySet()) {
            List<ScanTableCell> value = row.getValue();
            int rowLen = value.size();
            if (rowLen > resultRowLen) {
                resultRowLen = rowLen;
                patternRow = value;
            }
        }

        return patternRow;
    }

    public Integer getColKey(ScanTableCell cell, HashMap<Integer, List<ScanTableCell>> columns) {
        if (columns.size() > 0) {
            for (Map.Entry<Integer, List<ScanTableCell>> row : columns.entrySet()) {
                int key = row.getKey();
                if (key + delta >= cell.x && cell.x >= key - delta) {
                    return key;
                }
            }
        }
        return null;
    }

    public List<ScanTableCell> getPatternColumn(Map<Integer, List<ScanTableCell>> rows) {
        HashMap<Integer, List<ScanTableCell>> columns = new HashMap<>();
        List<ScanTableCell> flattenCells = new ArrayList<>();
        int patternColumnKey = 0;
        int maxLength = 0;

        for (Map.Entry<Integer, List<ScanTableCell>> row : rows.entrySet()) {
            flattenCells.addAll(row.getValue());
        }

        for (ScanTableCell cell : flattenCells) {
            Integer key = getColKey(cell, columns);
            if (key == null) {
                List<ScanTableCell> column = new ArrayList<>();
                column.add(cell);
                columns.put(cell.x, column);
            } else {
                columns.get(key).add(cell);
            }
        }

        for (Map.Entry<Integer, List<ScanTableCell>> column : columns.entrySet()) {
            int colLen = column.getValue().size();
            if (colLen > maxLength) {
                maxLength = colLen;
                patternColumnKey = column.getKey();
            }
        }

        List<ScanTableCell> patternColumn = columns.get(patternColumnKey);
        return patternColumn;
    }

    public int getSameColumnIndex(int x, List<ScanTableCell> patternRow) {
        int index = 0;
        for (ScanTableCell cell : patternRow) {
            if (x + delta >= cell.x && cell.x >= x - delta) {
                return index;
            }
            index++;
        }
        return index;
    }

    public int getSameRowIndex(int y, List<ScanTableCell> patternColumn) {
        int index = 0;
        for (ScanTableCell cell : patternColumn) {
            if (y + delta >= cell.y && cell.y >= y - delta) {
                return index;
            }
            index++;
        }
        return index;
    }

    public int calcColSpan(ScanTableCell cell, List<ScanTableCell> patternRow) {
        int colSpan = 1;
        int index = getSameColumnIndex(cell.x, patternRow);
        for (int i = index + 1; i < patternRow.size() - 1; i++) {
            if (patternRow.get(i).x < cell.x + cell.width) {
                colSpan++;
            }
        }
        return colSpan;
    }

    public int calcRowSpan(ScanTableCell cell, List<ScanTableCell> patternColumn) {
        int rowSpan = 1;
        int index = getSameRowIndex(cell.y, patternColumn);
        for (int i = index + 1; i < patternColumn.size() - 1; i++) {
            if (patternColumn.get(i).y < cell.y + cell.height) {
                rowSpan++;
            }
        }
        return rowSpan;
    }

    public List<List<Cell>> fillCellInfo(Map<Integer, List<ScanTableCell>> rows) throws Exception {
        List<List<Cell>> resultTable = new ArrayList<>();
        List<ScanTableCell> patternRow = getPatternRow(rows);
        List<ScanTableCell> patternColumn = getPatternColumn(rows);

        int rowIndex = 0;
        for (Map.Entry<Integer, List<ScanTableCell>> entry : rows.entrySet()) {
            List<ScanTableCell> value = entry.getValue();
            int cellIndex = 0;

            for (ScanTableCell cell : value) {
                int colSpan = calcColSpan(cell, patternRow);
                int rowSpan = calcRowSpan(cell, patternColumn);
                String text = "";
                if (!ImageHelper.isBlank(cell.image)) {                    
                    BufferedImage preparedImage = ImageHelper.getPreparedImage(cell.image);
                    text = tesseract.ocr(preparedImage);
                }
                
                Cell resultCell = new Cell(cell.width, cell.height, colSpan, rowSpan, rowIndex, cellIndex, text);
                if (rowIndex == resultTable.size()) {
                    resultTable.add(new ArrayList<>());
                }
                
                List<Cell> currentRow = resultTable.get(rowIndex);
                currentRow.add(resultCell);
                cellIndex++;
            }
            rowIndex++;
        }
    
        return resultTable;
    }
    
    public List<TableResponse> getTables(List<List<ImgCell>> tables) throws Exception {
        List<TableResponse> resultTables = new ArrayList<>();
        int tableIndex = 0;
        for (List<ImgCell> cells : tables) {
            Map<Integer, List<ScanTableCell>> sortedRows = getSortedRows(cells);
            List<List<Cell>> filledTable = fillCellInfo(sortedRows);

            int countCells = filledTable.stream().mapToInt(r -> r.size()).sum();
            int countFilledCells = filledTable.stream().mapToInt(r -> (int)r.stream().filter(c -> !c.getText().isEmpty()).count()).sum();            
            if(countFilledCells * 2 < countCells)
                continue;
            
            TableResponse pdfTable = new TableResponse();
            List<TableResponseCell> resCells = new ArrayList<>();
            for (List<Cell> filledCells : filledTable) {
                if(filledCells.isEmpty())
                    continue;
                
                if(filledCells.size() == 1 && filledCells.get(0).getText().isEmpty())
                    continue;
                
                for (Cell filledCell : filledCells) {
                    TableResponseCell cell = new TableResponseCell(
                            filledCell.getText(),
                            filledCell.getWidth(),
                            filledCell.getRownum(),
                            filledCell.getColnum(),
                            filledCell.getColspan(),
                            filledCell.getRowspan());
                    resCells.add(cell);
                }
            }

            pdfTable.setCells(resCells);
            pdfTable.setId(tableIndex);

            resultTables.add(pdfTable);
            tableIndex++;
        }
        return resultTables;
    }

    public List<TableResponse> getTablesList(Mat image) throws Exception {
        List<List<ImgCell>> tableWithCells = getTablesWithCells(image);
        return getTables(tableWithCells);
    }
}
