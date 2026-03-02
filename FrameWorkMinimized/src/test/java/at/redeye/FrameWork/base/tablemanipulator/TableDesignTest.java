package at.redeye.FrameWork.base.tablemanipulator;

import at.redeye.FrameWork.base.prm.bindtypes.DBConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class TableDesignTest {
    @Test
    void should_shift_edited_rows_on_removal() {
        TableDesign systemUnderTest = new TableDesign(null, emptyList());
        systemUnderTest.addRows(List.of(
                new DBConfig("row zero", "0", null),
                new DBConfig("row one", "1", null),
                new DBConfig("row two", "2", null)
        ));
        systemUnderTest.edited_rows.add(0);
        systemUnderTest.edited_rows.add(2);

        systemUnderTest.remove(1);

        assertThat(systemUnderTest.edited_rows).containsExactlyInAnyOrder(0, 1);
    }
}