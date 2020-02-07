#!/usr/bin/env bash

sbt clean scalastyle coverage test coverageReport
