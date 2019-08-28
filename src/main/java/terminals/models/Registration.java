package terminals.models;

public class Registration {
    private int recordId;
    private int terminalId;
    private int userId;
    private int whoGaveId;

    private String userLogin; //2
    private String userFullName; //3
    private String whoGave; //4
    private String whoReceived; //5
    private String terminalRegistrationId; //1
    private String startDate; //6
    private String endDate; //7


 public Registration(int terminalId, int userId, int whoGaveId) {
  this.terminalId = terminalId;
  this.userId = userId;
  this.whoGaveId = whoGaveId;
 }


 public Registration(String userLogin, String terminalRegistrationId) {
  this.userLogin = userLogin;
  this.terminalRegistrationId = terminalRegistrationId;
 }

 public Registration(String userLogin, String userFullName, String whoGave,
                     String whoReceived, String terminalRegistrationId, String startDate, String endDate) {
  this.userLogin = userLogin;
  this.userFullName = userFullName;
  this.whoGave = whoGave;
  this.whoReceived = whoReceived;
  this.terminalRegistrationId = terminalRegistrationId;
  this.startDate = startDate;
  this.endDate = endDate;
 }

 public int getRecordId() {
  return recordId;
 }

 public void setRecordId(int recordId) {
  this.recordId = recordId;
 }

 public int getTerminalId() {
  return terminalId;
 }

 public void setTerminalId(int terminalId) {
  this.terminalId = terminalId;
 }

 public int getUserId() {
  return userId;
 }

 public void setUserId(int userId) {
  this.userId = userId;
 }

 public String getUserLogin() {
  return userLogin;
 }

 public void setUserLogin(String userLogin) {
  this.userLogin = userLogin;
 }

 public String getUserFullName() {
  return userFullName;
 }

 public void setUserFullName(String userFullName) {
  this.userFullName = userFullName;
 }

 public String getWhoGave() {
  return whoGave;
 }

 public void setWhoGave(String whoGave) {
  this.whoGave = whoGave;
 }

 public String getWhoReceived() {
  return whoReceived;
 }

 public void setWhoReceived(String whoReceived) {
  this.whoReceived = whoReceived;
 }

 public String getTerminalRegistrationId() {
  return terminalRegistrationId;
 }

 public void setTerminalRegistrationId(String terminalRegistrationId) {
  this.terminalRegistrationId = terminalRegistrationId;
 }

 public String getStartDate() {
  return startDate;
 }

 public void setStartDate(String startDate) {
  this.startDate = startDate;
 }

 public String getEndDate() {
  return endDate;
 }

 public void setEndDate(String endDate) {
  this.endDate = endDate;
 }

 public int getWhoGaveId() {
  return whoGaveId;
 }

 public void setWhoGaveId(int whoGaveId) {
  this.whoGaveId = whoGaveId;
 }
 @Override
 public String toString() {
  return "Registration{" +
          "userLogin='" + userLogin + '\'' +
          ", userFullName='" + userFullName + '\'' +
          ", whoGave='" + whoGave + '\'' +
          ", whoReceived='" + whoReceived + '\'' +
          ", terminalRegistrationId='" + terminalRegistrationId + '\'' +
          ", startDate='" + startDate + '\'' +
          ", endDate='" + endDate + '\'' +
          '}';
 }
}