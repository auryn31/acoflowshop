overview <- read.csv(file="overview_finish.csv", header=TRUE, sep=",")

jpeg("overview_avg_finish.jpeg")
yrange <- range(overview$aco, overview$aica, overview$neh)
xrange <- range(overview$jobs)

plot(overview$jobs, overview$aco, type="p", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
lines(overview$jobs, overview$aica, type="p", col="green")
lines(overview$jobs, overview$neh, type="p", col="red")
box()

title(main="", xlab="Jobanzahl", ylab="Laufzeit in s", font.main=4, cex.lab=1.4, cex.axis=1.4, cex.main=1.4, cex.sub=1.4)
legend("topleft", legend=c("ACO", "AICA", "NEH"), col=c("blue", "green", "red"), lty=1:1, cex=1.5)