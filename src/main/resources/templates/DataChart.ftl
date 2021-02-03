<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <script src="./echarts/echarts.min.js"></script>
        <title>echartsDemo</title>
    </head>
    <body>
        <div id='echarts' style="width: 400px;height:400px;border:1px solid #eee"></div>
        <script>
            var charts = echarts.init(document.getElementById('echarts'))
            charts.setOption({
                title:{
                    text: 'hello Echarts'
                },
                xAxis: {
                    type: 'category',
                    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
                },
                yAxis: {
                    type: 'value'
                },
                series: [{
                    data: [120, 200, 150, 80, 70, 110, 130],
                    type: 'bar'
                }]
            })
        </script>
    </body>
</html>