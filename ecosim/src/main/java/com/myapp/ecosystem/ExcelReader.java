package com.myapp.ecosystem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Utility class that loads the first sheet of an .xlsx file whose header row
 * contains the 10 column names used by Organism:
 *
 *   name | type | calNeed | calGive | eats | eatenBy | cond1 | cond2 | cond3 | cond4
 *
 * Column order is irrelevant (header lookup is case-insensitive).
 * Cells in “eats” or “eatenBy” may contain comma-separated lists.
 */
public final class ExcelReader {

    private static final String LIST_DELIM = ",";   // split eats / eatenBy cells

    /** Returns the organisms contained in file <code>path</code>. */
    public static List<Organism> load(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path);
             Workbook wb   = WorkbookFactory.create(in)) {

            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) throw new IOException("Workbook is empty");

            // header → column index map
            Map<String,Integer> col = mapHeader(sheet.getRow(0));

            List<Organism> out = new ArrayList<>();
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;               // skip blank lines

                Organism o = new Organism();
                o.setName   (str(row,col,"name"));
                o.setType   (str(row,col,"type"));
                o.setCalNeed(flt(row,col,"calneed"));
                o.setCalGive(flt(row,col,"calgive"));
                o.setEats     (list(row,col,"eats"));
                o.setEatenBy  (list(row,col,"eatenby"));
                o.setCond1(str(row,col,"cond1"));
                o.setCond2(str(row,col,"cond2"));
                o.setCond3(str(row,col,"cond3"));
                o.setCond4(str(row,col,"cond4"));

                out.add(o);
            }
            return out;
        }
    }

    /* ── helpers ───────────────────────────────────────────────────────── */

    private static Map<String,Integer> mapHeader(Row header) {
        Map<String,Integer> m = new HashMap<>();
        for (Cell c : header) {
            String key = c.getStringCellValue().trim().toLowerCase();
            m.put(key, c.getColumnIndex());
        }
        return m;
    }

    private static String str(Row row, Map<String,Integer> col, String key) {
        Integer idx = col.get(key);
        if (idx == null) return "";
        Cell c = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return (c == null) ? "" : c.getStringCellValue().trim();
    }

    private static float flt(Row row, Map<String,Integer> col, String key) {
        String s = str(row,col,key);
        if (s.isEmpty()) return 0f;
        try { return Float.parseFloat(s); }
        catch (NumberFormatException n) { return 0f; }
    }

    private static List<String> list(Row row, Map<String,Integer> col, String key) {
        String raw = str(row,col,key);
        if (raw.isEmpty()) return List.of();
        String[] parts = raw.split(LIST_DELIM);
        List<String> out = new ArrayList<>(parts.length);
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }
}
