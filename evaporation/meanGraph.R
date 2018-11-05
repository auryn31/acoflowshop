evaporation003 <- read.csv(file="config_0_mean.csv", header=FALSE, sep=",")
evaporation004 <- read.csv(file="config_1_mean.csv", header=FALSE, sep=",")
evaporation005 <- read.csv(file="config_2_mean.csv", header=FALSE, sep=",")
evaporation006 <- read.csv(file="config_3_mean.csv", header=FALSE, sep=",")

iteration <- c(evaporation003[1]$V1)

jpeg('result_time_mean_iteration.jpg')
yrange <- range(evaporation003[2]$V2, 
    evaporation004[2]$V2, 
    evaporation005[2]$V2, 
    evaporation006[2]$V2)

xrange <- range(evaporation003[3]$V3, 
    evaporation004[3]$V3, 
    evaporation005[3]$V3, 
    evaporation006[3]$V3)

plot(iteration, evaporation003[2]$V2, type="l", ylim=yrange, col="blue", ann=FALSE)
lines(iteration, evaporation004[2]$V2, type="l", col="green")
lines(iteration, evaporation005[2]$V2, type="l", col="chocolate1")
lines(iteration, evaporation006[2]$V2, type="l", col="burlywood3")

title(main='Verdunstungsfaktor 50 Jobs', col.main="red", font.main=4)
title(xlab='Iteration')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("0.03", "0.04", "0.05", "0.06"), 
    col=c("blue", "green", "chocolate1", "burlywood3"),
    lty=1:1, cex=0.8)

jpeg('result_time_mean_calculationtime.jpg')
plot(evaporation003[3]$V3, evaporation003[2]$V2, type="l", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
lines(evaporation004[3]$V3, evaporation004[2]$V2, type="l", col="green")
lines(evaporation005[3]$V3, evaporation005[2]$V2, type="l", col="chocolate1")
lines(evaporation006[3]$V3, evaporation006[2]$V2, type="l", col="burlywood3")

box()

title(main='Verdunstungsfaktor 50 Jobs', col.main="red", font.main=4)
title(xlab='Laufzeit in s')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("0.03", "0.04", "0.05", "0.06"), 
    col=c("blue", "green", "chocolate1", "burlywood3"),
    lty=1:1, cex=0.8)