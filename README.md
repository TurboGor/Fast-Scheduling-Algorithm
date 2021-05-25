# Fast-Scheduling-Algorithm

Comp3100 Stage 2 Assignment

## How to run Stage2
Step1: Navigate to the `src` folder
Step2: Open a terminal inside the `src` folder
Step3: Ensure test_results has correct permissions
`sudo chmod u+rwx test_results`
Step4: Start the test file executable
`./test_results "java Client" -o co -c configs/`

---

## Report Link
https://github.com/TurboGor/Fast-Scheduling-Algorithm/blob/master/report.pdf

---

## Overview
ds-sim is a discrete-event simulator that has been developed primarily for leveraging scheduling algorithm design. It adopts a minimalist design explicitly taking into account modularity in that it uses the client-server model. The client-side simulator acts as a job scheduler while the server-side simulator simulates everything else including users (job submissions) and servers (job execution).

---

## How to run a simulation
1. run server `$ ds-server -c [CONFIG] [OPTION]...`
2. run client `$ ds-client -a [ALGORITHM] [OPTION]...`

---

## Usage Example
`$ ds-server -c ds-config01.xml -v brief`

`$ ds-client -a bf`

