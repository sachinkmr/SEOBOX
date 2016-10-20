package sachin.seobox.reporter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.relevantcodes.extentreports.LogStatus;

public class TestCase {
    private LogStatus status;
    private String id;
    private String name;
    private Date time, endedTime;
    private List<String> cats = new ArrayList<>();
    private String runDuration;
    private String desc;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRunDuration() {
	return this.runDuration;
    }

    public void setRunDuration(String runDuration) {
	this.runDuration = runDuration;
    }

    public Date getEndedTime() {
	return this.endedTime;
    }

    public void setEndedTime(Date endedTime) {
	this.endedTime = endedTime;
    }

    public List<String> getCats() {
	return this.cats;
    }

    public void setCats(String cat) {
	cats.add(cat);
    }

    public LogStatus getStatus() {
	return this.status;
    }

    public void setStatus(LogStatus status) {
	this.status = status;
    }

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Date getTime() {
	return this.time;
    }

    public void setTime(Date time) {
	this.time = time;
    }

}
