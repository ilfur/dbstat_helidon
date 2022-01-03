define(['ojs/ojcore'], function (oj) {
    var DeptFactory = {
        resourceUrl : 'http://127.0.0.1:8080/dbStat/params', 
        // Single Department Model
        createDeptModel : function () {
            var dept = oj.Model.extend( {
                urlRoot : this.resourceUrl, 
                idAttribute : "name"
            });
            return new dept();
        },
        // Departments Collection
        createDepartmentsCollection : function () {
            var departments = oj.Collection.extend( {
                url : this.resourceUrl, 
                model : this.createDeptModel(), 
                comparator : "name"
            });
            return new departments();
        }
    };
    return DeptFactory;
});
