initMatrixWithNEHTrue <- read.csv(file="config_0_mean.csv", header=FALSE, sep=",")
initMatrixWithNEHFalse <- read.csv(file="config_1_mean.csv", header=FALSE, sep=",")

iteration <- c(initMatrixWithNEHTrue[1]$V1)

jpeg('result_time_mean_iteration.jpg')
yrange <- range(initMatrixWithNEHTrue[2]$V2, 
    initMatrixWithNEHFalse[2]$V2)

xrange <- range(initMatrixWithNEHTrue[3]$V3, 
    initMatrixWithNEHFalse[3]$V3)

plot(iteration, initMatrixWithNEHTrue[2]$V2, type="l", ylim=yrange, col="blue", ann=FALSE)
lines(iteration, initMatrixWithNEHFalse[2]$V2, type="l", col="green")

title(main='Initialisierung mit NEH 50 Jobs', font.main=4)
title(xlab='Iteration')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("true", "false"), 
    col=c("blue", "green"),
    lty=1:1, cex=0.8)

jpeg('result_time_mean_calculationtime.jpg')
plot(initMatrixWithNEHTrue[3]$V3, initMatrixWithNEHTrue[2]$V2, type="l", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
lines(initMatrixWithNEHFalse[3]$V3, initMatrixWithNEHFalse[2]$V2, type="l", col="green")

box()

title(main='Initialisierung mit NEH 50 Jobs', font.main=4)
title(xlab='Laufzeit in s')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("true", "false"), 
    col=c("blue", "green"),
    lty=1:1, cex=0.8)