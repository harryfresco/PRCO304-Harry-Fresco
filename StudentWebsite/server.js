var express = require('express');
var app = express();

app.get('/', function (req, res) {
   
    var sql = require("mssql");

    // config for your database
    var config = {
        user: 'HFresco',
        password: 'PRCO304!',
        server: 'socem1.uopnet.plymouth.ac.uk', 
        database: 'PRCO304_HFresco' 
    };
    var userID = 1;
    var password = "password1";
    var sqlQuery = "SELECT StudentFirstName from dbo.student_table WHERE StudentID = ' "+userID+"' AND StudentPassword = '"+password+"'";
    // connect to your database
    sql.connect(config, function (err) {
    
        if (err) console.log(err);

        // create Request object
        var request = new sql.Request();
           
        // query to the database and get the records
        request.query(sqlQuery, function (err, recordset) {
            
            if (err) console.log(err)

            // send records as a response
            res.send(recordset);
            console.log(recordset);
        });
    });
});

var server = app.listen(9000, function () {
    console.log('Server is running..');
});