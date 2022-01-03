define(['ojs/ojcore', 'knockout', 'jquery', 'dbDetailFactory', 'ojs/ojtable', 'ojs/ojinputtext', 'ojs/ojformlayout', ,'ojs/ojknockout', 'ojs/ojknockout-model'], 
function (oj, ko, $, dbDetailFactory) {
    function DashboardViewModel() {
        var self = this;
        self.dbUser = ko.observable('yada');
        self.dbUrl = ko.observable('yada');
        self.buttonTxt = ko.observable('Restart Container');
  $.ajax({
    type: "GET",
    //headers: rootViewModel.getHeaders().headers,
    url: "http://127.0.0.1:8080/dbStat",
    success: function(res) {
        var resData = res;
        self.dbUser(res.DBuser);
        self.dbUrl(res.DBurl);
      },
    failure: function(jqXHR, textStatus, errorThrown) {
        alert(textStatus);
      }
    });
    self.buttonClick = function(event) {
      $.ajax({
    type: "GET",
    //headers: rootViewModel.getHeaders().headers,
    url: "http://127.0.0.1:8080/dbStat/restart",
    success: function(res) {
        var resData = res;
        self.buttonTxt("Restarting: "+res.restart);
      },
    failure: function(jqXHR, textStatus, errorThrown) {
        alert(textStatus);
      }
    });
      return true;
    }
    }
    return new DashboardViewModel;
});
