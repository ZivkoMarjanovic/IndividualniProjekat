function init(stats) {
	let labelsStats = [];
	let dataStats = [];
	stats.forEach(function(oneDay) {
		labelsStats.push(oneDay.day);
		dataStats.push(oneDay.total);
	});
	var data = {
	  labels: labelsStats,
	  datasets: [{
	    label: "Total orders amount per day in eur.",
	    backgroundColor: "rgba(132, 140, 207,0.6)",
	    borderColor: "rgba(50, 111, 168,1)",
	    borderWidth: 2,
	    hoverBackgroundColor: "rgba(255,99,132,0.4)",
	    hoverBorderColor: "rgba(255,99,132,1)",
	    data: dataStats,
	  }]
	};
	
	var options = {
	  maintainAspectRatio: false,
	  scales: {
	    y: {
	      stacked: true,
	      grid: {
	        display: true,
	        color: "rgba(255,99,132,0.2)"
	      }
	    },
	    x: {
	      grid: {
	        display: false
	      }
	    }
	  }
	};
	
	new Chart('chart', {
	  type: 'bar',
	  options: options,
	  data: data
	});
	
};