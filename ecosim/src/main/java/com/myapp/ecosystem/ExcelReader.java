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

    private static final String LIST_DELIM = ",";   // The input on split eats / eatenBy cells is separated with ,

    // Returns the organisms contained in the specified file
    public static List<Organism> load(Path path) {
        List<Organism> out = new ArrayList<>();

        try (InputStream in = Files.newInputStream(path);
            Workbook     wb = WorkbookFactory.create(in)) {

            // Data must be on first sheet
            Sheet sheet = wb.getSheetAt(0);

            if (sheet == null) {
                System.err.println("Workbook is empty: " + path);
                return out;
            }

            // Get the header row into the map
            Map<String, Integer> col = mapHeader(sheet.getRow(0));

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;           // skip blank lines

                // Create a new organism object, populate them and add them to the list
                Organism o = new Organism();
                o.setName     (str (row, col, "name"));
                o.setType     (str (row, col, "type"));
                o.setCalNeed  (flt (row, col, "calneed"));
                o.setCalGive  (flt (row, col, "calgive"));
                o.setEats     (list(row, col, "eats"));
                o.setEatenBy  (list(row, col, "eatenby"));
                o.setCond1    (str (row, col, "cond1"));
                o.setCond2    (str (row, col, "cond2"));
                o.setCond3    (str (row, col, "cond3"));
                o.setCond4    (str (row, col, "cond4"));

                out.add(o);
            }

        } catch (IOException e) {
            System.err.println("Error reading " + path + ": " + e.getMessage());
            e.printStackTrace();
        }

        return out;
    }

    /* ── helpers ───────────────────────────────────────────────────────── */

        /** Build a map: lowercase-header-name → column index (0-based). */
    private static Map<String, Integer> mapHeader(Row header) {
        Map<String, Integer> m = new HashMap<>();
        for (Cell c : header) {
            String key = c.getStringCellValue().trim().toLowerCase(); // header text
            m.put(key, c.getColumnIndex());                           // remember column #
        }
        return m;
    }

    /** Read a cell as String; return "" if the column is missing or blank. */
    private static String str(Row row, Map<String,Integer> col, String key) {
        Integer idx = col.get(key);                                   // which column?
        if (idx == null) return "";                                   // header not found
        Cell c = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return (c == null) ? "" : c.getStringCellValue().trim();      // blank → ""
    }

    /** Read a cell as float; blank / bad number → 0 f. */
    private static float flt(Row row, Map<String,Integer> col, String key) {
        String s = str(row, col, key);          // reuse str() to get raw text
        if (s.isEmpty()) return 0f;
        try { return Float.parseFloat(s); }      // parse numeric text
        catch (NumberFormatException e) { return 0f; }  // bad value → 0
    }

    /** Read a comma-separated cell into a trimmed List<String>. */
    private static List<String> list(Row row, Map<String,Integer> col, String key) {
        String raw = str(row, col, key);         // full cell text
        if (raw.isEmpty()) return List.of();     // empty cell → empty list
        String[] parts = raw.split(LIST_DELIM);  // split on comma
        List<String> out = new ArrayList<>(parts.length);
        for (String p : parts) {
            String t = p.trim();
            if (!t.isEmpty()) out.add(t);        // ignore empty fragments
        }
        return out;
    }
}
