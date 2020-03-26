
function login(req, res) {
   
    var uname = document.getElementById('uname');
    var psw = document.getElementById('psw');
    var sql = require("mssql");

    // config for your database
    var config = {
        user: 'HFresco',
        password: 'PRCO304!',
        server: 'socem1.uopnet.plymouth.ac.uk', 
        database: 'PRCO304_HFresco' 
    };
    var sqlQuery = "SELECT * from dbo.student_table WHERE StudentID = "+uname+" AND StudentPassword = "+psw+"";
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
            console.log(res);
        });
        
    });
};
