define(['ojs/ojcore', 'knockout', 'jquery', 'customersFactory', 'ojs/ojtable', 'ojs/ojcollectiontabledatasource'], 
function (oj, ko, $, customersFactory) {
    function CustomerViewModel() {
        var self = this;
        self.deptCollection = customersFactory.createDepartmentsCollection();
        self.datasource = ko.observable()
        // Setting collection in row and column format to show in JET table
        self.datasource(new oj.CollectionTableDataSource(this.deptCollection));
    }
    return new CustomerViewModel;
});